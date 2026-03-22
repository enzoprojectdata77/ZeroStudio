import os
import argparse
import json
import re
from openai import OpenAI
from concurrent.futures import ThreadPoolExecutor, as_completed

# --- 配置 ---
CHUNK_SIZE = 30  
MAX_WORKERS = 6  
TARGET_MODEL = "tencent/Hunyuan-MT-7B"

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

def translate_chunk(client, model, chunk_data, target_lang):
    prompt = f"Translate to {target_lang}. Keep placeholders (%s, %1$d) exactly. Return JSON."
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
        return {k: fix_format(v) for k, v in batch_res.items()}
    except Exception as e:
        print(f"   ⚠️ Chunk Error: {e}")
        return {k: v for k, v in chunk_data.items()}

def process_file_with_regex(client, model, file_path, target_lang):
    """使用正则读取和替换，确保格式、注释、空行不动"""
    if not os.path.exists(file_path): return
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # 正则匹配: <string name="ID">VALUE</string>
    # 排除 translatable="false" 的行
    pattern = re.compile(r'<string\s+name="([^"]+)"(?![^>]*translatable="false")>(.*?)</string>', re.DOTALL)
    matches = pattern.findall(content)

    to_translate = {name: val for name, val in matches if val and not val.startswith("@string/")}
    if not to_translate: return

    print(f"🚀 [File] {file_path} | Translating {len(to_translate)} items...")

    # 分片并行翻译
    keys = list(to_translate.keys())
    chunks = [{k: to_translate[k] for k in keys[i:i+CHUNK_SIZE]} for i in range(0, len(keys), CHUNK_SIZE)]
    translated_map = {}
    
    with ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        futures = [executor.submit(translate_chunk, client, model, c, target_lang) for c in chunks]
        for f in as_completed(futures):
            translated_map.update(f.result())

    # --- 按顺序回填内容 ---
    new_content = content
    for name, trans_val in translated_map.items():
        # 精准匹配该 ID 的标签内容进行替换，保持前后文不动
        # 使用 lambda 避免反斜杠转义问题
        specific_pattern = re.compile(f'(<string\\s+name="{re.escape(name)}"[^>]*>)(.*?)(</string>)', re.DOTALL)
        new_content = specific_pattern.sub(rf'\1{trans_val}\3', new_content)

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    print(f"✅ [Done] {file_path}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--api_key", required=True)
    parser.add_argument("--base_url", default="https://api.siliconflow.cn")
    parser.add_argument("--files", default="strings.xml")
    args = parser.parse_args()

    client = OpenAI(api_key=args.api_key, base_url=args.base_url)
    target_files = [f.strip() for f in args.files.replace(",", " ").split() if f.strip()]
    
    module_paths = ["core/resources/src/main/res/", "core/chatai/resources/src/main/res/"]
    
    for module in module_paths:
        for fname in target_files:
            for folder in LANG_FOLDERS:
                f_path = os.path.join(module, folder, fname)
                process_file_with_regex(client, TARGET_MODEL, f_path, folder)
