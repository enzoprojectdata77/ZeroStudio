#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Tree-sitter Structured SCM Generator
@author android_zero
run py: python3 generate_structured_scm.py -i typeScript/src/main/cpp/typescript/src/node-types.json -o typeScript_highlights.scm
此脚本深度解析 node-types.json 的结构上下文(包含 fields 和层级关系)，
准确无误地推断出函数、类、参数、属性等不同结构下的符号，生成标准的高级 highlights.scm。
"""

import json
import argparse
import sys
import re

def escape_string(s):
    """安全地转义 Tree-sitter 字符串中的特殊字符"""
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('\n', '\\n').replace('\r', '\\r').replace('\t', '\\t')

def determine_capture(parent_type, field_name, child_type):
    """
    【核心逻辑】智能结构与语义推断引擎
    根据父节点类型、字段名和子节点类型，精准判断应该赋予什么高亮标签 (@capture)
    """
    parent_lower = parent_type.lower()
    field_lower = field_name.lower()
    child_lower = child_type.lower()

    # 1. 明确的类型定义 (Class, Interface, Struct, Enum)
    if field_lower in ['name', 'type'] and any(x in parent_lower for x in['class', 'interface', 'struct', 'enum', 'type', 'trait']):
        return "type"
    if field_lower == 'type' or 'type' in child_lower:
        return "type"

    # 2. 函数与方法 (Function, Method, Call)
    if field_lower in ['name', 'function', 'method']:
        if 'call' in parent_lower or 'invocation' in parent_lower:
            return "function.call"
        elif any(x in parent_lower for x in ['func', 'method', 'constructor']):
            return "function"

    # 3. 参数 (Parameters)
    if field_lower in ['parameters', 'parameter', 'args', 'argument'] or 'parameter' in parent_lower:
        return "variable.parameter"

    # 4. 属性与字段 (Properties & Fields)
    if field_lower in ['property', 'field', 'key'] or 'property' in parent_lower:
        return "property"
        
    # 5. 常量与宏 (Constants & Macros)
    if 'const' in parent_lower or 'macro' in parent_lower:
        return "constant"
        
    # 6. 注解与装饰器 (Annotations & Decorators)
    if 'annotation' in parent_lower or 'decorator' in parent_lower:
        return "attribute"

    # 7. 字面量兜底 (Literals)
    if 'string' in child_lower: return "string"
    if 'number' in child_lower or 'integer' in child_lower or 'float' in child_lower: return "number"
    if 'bool' in child_lower: return "boolean"
    if 'comment' in child_lower: return "comment"

    # 8. 标识符兜底
    if 'identifier' in child_lower or 'name' in child_lower:
        return "variable"

    return None

def generate_scm(node_types_path, output_path):
    try:
        with open(node_types_path, 'r', encoding='utf-8') as f:
            nodes = json.load(f)
    except Exception as e:
        print(f"无法读取 node-types.json: {e}")
        sys.exit(1)

    unnamed_keywords = set()
    unnamed_operators = set()
    unnamed_brackets = set()
    unnamed_delimiters = set()
    
    # 存储结构化查询的集合，防止重复
    structured_queries = {
        "Types": set(),
        "Functions": set(),
        "Variables": set(),
        "Properties": set(),
        "Constants": set(),
        "Literals": set(),
        "Attributes": set(),
    }

    # 用于正则表达式匹配符号
    operator_chars = re.compile(r'^[+\-*/%=<>!&|^~?]+$')
    bracket_chars = re.compile(r'^[()\[\]{}]+$')
    delimiter_chars = re.compile(r'^[,;:]+$')

    print(f"正在深度分析 {node_types_path} 中的 {len(nodes)} 个节点结构...")

    for node in nodes:
        node_type = node.get("type", "")
        is_named = node.get("named", False)

        if not node_type:
            continue

        # ==========================================
        # 阶段一：扫描无名节点 (符号、标点、关键字)
        # ==========================================
        if not is_named:
            if node_type.isalpha() and len(node_type) > 1:
                unnamed_keywords.add(node_type)
            elif bracket_chars.match(node_type):
                unnamed_brackets.add(node_type)
            elif delimiter_chars.match(node_type):
                unnamed_delimiters.add(node_type)
            elif operator_chars.match(node_type):
                unnamed_operators.add(node_type)
            elif node_type.isalpha() and len(node_type) == 1:
                unnamed_keywords.add(node_type)
            continue

        # ==========================================
        # 阶段二：扫描命名节点的属性化关联关系 (Fields)
        # ==========================================
        fields = node.get("fields", {})
        for field_name, field_info in fields.items():
            for child_info in field_info.get("types",[]):
                child_type = child_info.get("type", "")
                child_named = child_info.get("named", False)
                
                # 获取准确的高亮标签
                capture_tag = determine_capture(node_type, field_name, child_type)
                
                if capture_tag:
                    # 构造结构化 SCM 字符串
                    child_syntax = f"({child_type})" if child_named else f'"{escape_string(child_type)}"'
                    query_str = f"({node_type} {field_name}: {child_syntax} @{capture_tag})"
                    
                    # 放入对应的分类集合中
                    if "type" in capture_tag: structured_queries["Types"].add(query_str)
                    elif "function" in capture_tag: structured_queries["Functions"].add(query_str)
                    elif "property" in capture_tag: structured_queries["Properties"].add(query_str)
                    elif "constant" in capture_tag: structured_queries["Constants"].add(query_str)
                    elif "variable" in capture_tag: structured_queries["Variables"].add(query_str)
                    elif "attribute" in capture_tag: structured_queries["Attributes"].add(query_str)
                    else: structured_queries["Literals"].add(query_str)

        # ==========================================
        # 阶段三：扫描基础独立节点作为兜底 (Fallbacks)
        # ==========================================
        node_lower = node_type.lower()
        fallback_query = f"({node_type}) "
        if "string" in node_lower and not "fragment" in node_lower:
            structured_queries["Literals"].add(fallback_query + "@string")
        elif "comment" in node_lower:
            structured_queries["Literals"].add(fallback_query + "@comment")
        elif "number" in node_lower or "integer" in node_lower or "float" in node_lower:
            structured_queries["Literals"].add(fallback_query + "@number")
        elif "boolean" in node_lower or "true" in node_lower or "false" in node_lower:
            structured_queries["Literals"].add(fallback_query + "@boolean")
        elif "identifier" in node_lower:
            structured_queries["Variables"].add(fallback_query + "@variable")

    # ==========================================
    # 写入最终的 .scm 文件
    # ==========================================
    try:
        with open(output_path, 'w', encoding='ascii') as f:
            f.write("; Automatically generated by Advanced Tree-sitter SCM Generator\n")
            f.write("; @author android_zero\n")
            f.write("; This file contains accurate structural contexts based on AST fields.\n\n")

            # 写入结构化节点
            for category, queries in structured_queries.items():
                if not queries: continue
                f.write(f"; {'=' * 38}\n")
                f.write(f"; {category}\n")
                f.write(f"; {'=' * 38}\n\n")
                for query in sorted(list(queries)):
                    f.write(f"{query}\n")
                f.write("\n")

            # 辅助函数：写入数组组
            def write_array_group(items, capture_name, title):
                if not items: return
                f.write(f"; {title}\n")
                f.write("[\n")
                for item in sorted(list(items)):
                    f.write(f'  "{escape_string(item)}"\n')
                f.write(f"] @{capture_name}\n\n")

            # 写入标点与关键字
            f.write(f"; {'=' * 38}\n")
            f.write(f"; Keywords, Operators & Punctuation\n")
            f.write(f"; {'=' * 38}\n\n")
            
            write_array_group(unnamed_keywords, "keyword", "Keywords")
            write_array_group(unnamed_operators, "operator", "Operators")
            write_array_group(unnamed_brackets, "punctuation.bracket", "Brackets")
            write_array_group(unnamed_delimiters, "punctuation.delimiter", "Delimiters")

        print(f"\n解析完成！已生成高度结构化的 SCM 文件：{output_path}")

    except Exception as e:
        print(f"写入文件出错: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Generate structural highlights.scm from node-types.json")
    parser.add_argument("-i", "--input", required=True, help="Path to src/node-types.json")
    parser.add_argument("-o", "--output", required=True, help="Path to output highlights.scm")

    args = parser.parse_args()
    generate_scm(args.input, args.output)