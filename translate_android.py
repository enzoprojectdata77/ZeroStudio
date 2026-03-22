import os
import argparse
import json
import re
import time
from openai import OpenAI

# --- 核心配置 ---
CHUNK_SIZE = 30  
TARGET_MODEL = "tencent/Hunyuan-MT-7B"

# 你指定的语言文件夹列表
LANG_FOLDERS = [
    "values", "values-ar-rSA", "values-bn-rIN", "values-de-rDE", "values-es-rES", 
    "values-fa", "values-fil", "values-fr-rFR", "values-hi-rIN", "values-in-rID", 
    "values-it", "values-ja", "values-ko", "values-ml", "values-pl", "values-pt-rBR", 
    "values-ro-rRO", "values-ru-rRU", "values-ta", "values-th", "values-tm-rTM", 
    "values-tr-rTR", "values-uk", "values-vi", "values-zh-rCN", "values-zh-rTW"
]

def fix_format(text):
    if not text: return ""
    result = text.replace("'", "\\'")
    result = re.sub(r"%\s*([sd])", r"%\1", result)
    result = re.sub(r"%\s*(\d+\$)\s*([sd])", r"%\1\2", result)
    return result.replace("] ] >", "]]>").replace("\\ n", "\\n")

def safe_translate(client, model, chunk_data, target_lang):
    """单次请求逻辑，增加重试和延时防止 RPM 限制"""
    prompt = f"Translate to {target_lang}. Keep placeholders (%s, %1$d) exactly. Return JSON."
    
    for attempt in range(3): # 最多重试 3 次
        try:
            response = client.chat.completions.create(
                model=model,
                messages=[
                    {"role": "system", "content": "You are a professional Android translator. Return JSON only."},
                    {"role": "user", "content": f"{prompt}\n\n{json.dumps(chunk_data, ensure_ascii=False)}"}
                ],
                response_format={"type": "json_object"},
                temperature=0.1
            )
            batch_res = json.loads(response.choices.message.content)
            
            print(f"   ✅ Batch translated ({len(batch_res)} items)")
            # 适当留白，防止触发频率限制
            time.sleep(0.5) 
            return {k: fix_format(v) for k, v in batch_res.items()}
            
        except Exception as e:
            print(f"   ⚠️ Attempt {attempt+1} failed: {e}")
            if "balance" in str(e).lower():
                print("   ❌ 余额不足或未实名，请检查账户。")
                return {k: v for k, v in chunk_data.items()}
            time.sleep(2) # 失败后等久一点
            
    return {k: v for k, v in chunk_data.items()}

def process_file_sequentially(client, model, file_path, target_lang):
    """顺序处理文件：读取 -> 提取 -> 翻译 -> 安全回填"""
    if not os.path.exists(file_path): return
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # 提取 <string> 标签，排除 translatable="false"
    pattern = re.compile(r'(<string\s+name="([^"]+)"(?![^>]*translatable="false")>)(.*?)(</string>)', re.DOTALL)
    
    matches = list(pattern.finditer(content))
    to_translate_kv = {}
    for m in matches:
        key, val = m.group(2), m.group(3)
        if val and not val.startswith("@string/"):
            to_translate_kv[key] = val

    if not to_translate_kv: return

    print(f"\n📂 [Processing] {file_path} | {len(to_translate_kv)} items")

    # 顺序分片翻译（不再使用多线程，确保 API 稳定）
    keys = list(to_translate_kv.keys())
    translated_map = {}
    for i in range(0, len(keys), CHUNK_SIZE):
        chunk_keys = keys[i:i+CHUNK_SIZE]
        chunk_data = {k: to_translate_kv[k] for k in chunk_keys}
        res = safe_translate(client, model, chunk_data, target_lang)
        translated_map.update(res)

    # --- 安全回填（弃用 re.sub，改用索引查找替换，防止 \S 等正则报错） ---
    new_content = content
    # 从后往前替换，防止索引偏移影响后续替换
    sorted_matches = sorted(list(pattern.finditer(content)), key=lambda x: x.start(), reverse=True)
    
    for m in sorted_matches:
        key = m.group(2)
        if key in translated_map:
            prefix = m.group(1) # <string name="...">
            suffix = m.group(4) # </string>
            new_val = translated_map[key]
            # 拼装新标签
            new_tag = f"{prefix}{new_val}{suffix}"
            # 替换原始位置
            new_content = new_content[:m.start()] + new_tag + new_content[m.end():]

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--base_url", default="https://api.siliconflow.cn")
    parser.add_argument("--files", default="strings.xml")
    args = parser.parse_args()

    api_key = os.getenv("SF_API_KEY")
    client = OpenAI(api_key=api_key, base_url=args.base_url)
    target_files = [f.strip() for f in args.files.replace(",", " ").split() if f.strip()]
    
    # 一个模块接一个模块，一个文件接一个文件，一个语言接一个语言运行
    module_paths = ["core/resources/src/main/res/", "core/chatai/resources/src/main/res/"]
    
    for module in module_paths:
        for fname in target_files:
            for folder in LANG_FOLDERS:
                f_path = os.path.join(module, folder, fname)
                process_file_sequentially(client, TARGET_MODEL, f_path, folder)

    print("\n🎉 All tasks finished! Sequentially and safely.")
