#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Tree-sitter AST to SCM Generator
@author android_zero

run py：python3 generate_ast_scm.py -i swift/src/node-types.json -o swift_highlights.scm

This script deeply analyzes the `fields` and relationships in `node-types.json`
to intelligently deduce structural contexts (Classes, Functions, Parameters, etc.)
and generates an accurate, conflict-free, pure ASCII `highlights.scm`.
"""

import json
import argparse
import sys
import re
from collections import defaultdict

def escape_string(s):
    """Safely escape strings for SCM to prevent parser crashes."""
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('\n', '\\n').replace('\r', '\\r').replace('\t', '\\t')

def infer_semantic_capture(parent_type, field_name, child_type, is_named):
    """
    【核心大脑】语义与结构推理引擎
    通过分析父节点(Parent)、字段名(Field)和子节点(Child)的上下文，精准分配高亮标签。
    """
    p = parent_type.lower()
    f = field_name.lower()
    c = child_type.lower()

    # 如果子节点是无名节点（比如关键字 "class", "func", "return"），如果是字母组成，一律视为关键字
    if not is_named:
        if c.isalpha() and len(c) > 1:
            return "keyword"
        return None

    # 1. Types & Classes (类型、类、接口、协议)
    # 如果字段是 type/superclass，或者父节点是类/协议等声明且字段是 name
    if f in['type', 'superclass', 'base', 'inherits_from', 'constrained_type']:
        return "type"
    if f == 'name' and any(x in p for x in['class', 'struct', 'enum', 'protocol', 'interface', 'trait', 'typealias', 'generic']):
        return "type"
    if 'type' in c and not 'literal' in c:
        return "type"

    # 2. Functions & Methods (函数、方法声明与调用)
    if f in ['name', 'function', 'method', 'selector']:
        if 'call' in p or 'invocation' in p:
            # 这是一个调用 (e.g., call_expression)
            return "function.call"
        if any(x in p for x in ['func', 'method', 'constructor', 'init', 'macro']):
            # 这是一个声明
            return "function"

    # 3. Parameters & Arguments (参数与变量)
    if f in['parameter', 'parameters', 'arg', 'args', 'argument']:
        return "variable.parameter"
    if 'parameter' in p and f == 'name':
        return "variable.parameter"

    # 4. Properties & Fields (属性、对象字段)
    if f in ['property', 'field', 'key']:
        return "property"

    # 5. Constants (常量)
    if 'const' in p and f == 'name':
        return "constant"

    return None

def generate_scm(input_file, output_file):
    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            ast_nodes = json.load(f)
    except Exception as e:
        print(f"Error reading {input_file}: {e}")
        sys.exit(1)

    # 集合存储
    structured_queries = defaultdict(set)
    unnamed_keywords = set()
    unnamed_operators = set()
    unnamed_brackets = set()
    unnamed_delimiters = set()
    fallbacks = set()

    # 正则表达式用于符号分类
    regex_operators = re.compile(r'^[+\-*/%=<>!&|^~?]+$')
    regex_brackets = re.compile(r'^[()\[\]{}]+$')
    regex_delimiters = re.compile(r'^[,;:\.]+$')

    print(f"Scanning {len(ast_nodes)} AST nodes from {input_file}...")

    for node in ast_nodes:
        parent_type = node.get("type", "")
        is_named = node.get("named", False)

        if not parent_type:
            continue

        # ==========================================
        # 阶段 1: 无名节点分类 (标点、操作符、关键字)
        # ==========================================
        if not is_named:
            if parent_type.isalpha() and len(parent_type) > 1:
                unnamed_keywords.add(parent_type)
            elif parent_type.startswith('#') or parent_type.startswith('@'):
                unnamed_keywords.add(parent_type) # 宏指令如 #if, @autoclosure
            elif regex_brackets.match(parent_type):
                unnamed_brackets.add(parent_type)
            elif regex_delimiters.match(parent_type):
                unnamed_delimiters.add(parent_type)
            elif regex_operators.match(parent_type):
                unnamed_operators.add(parent_type)
            elif parent_type.isalpha() and len(parent_type) == 1:
                unnamed_keywords.add(parent_type)
            continue

        # ==========================================
        # 阶段 2: 根据 Fields 提取结构化树查询
        # ==========================================
        fields = node.get("fields", {})
        for field_name, field_data in fields.items():
            for child in field_data.get("types",[]):
                child_type = child.get("type", "")
                child_named = child.get("named", False)

                capture_tag = infer_semantic_capture(parent_type, field_name, child_type, child_named)

                if capture_tag:
                    # 组装 SCM: (parent_type field_name: (child_type)) @capture
                    child_syntax = f"({child_type})" if child_named else f'"{escape_string(child_type)}"'
                    scm_query = f"({parent_type} {field_name}: {child_syntax} @{capture_tag})"
                    structured_queries[capture_tag].add(scm_query)

        # ==========================================
        # 阶段 3: 提取基础节点作为 Fallback 兜底
        # ==========================================
        p_lower = parent_type.lower()
        fallback_query = f"({parent_type}) "
        if "string" in p_lower and "literal" in p_lower:
            fallbacks.add(fallback_query + "@string")
        elif "comment" in p_lower or "doc" in p_lower:
            fallbacks.add(fallback_query + "@comment")
        elif "integer" in p_lower or "float" in p_lower or "number" in p_lower or "hex" in p_lower or "oct" in p_lower or "bin" in p_lower:
            fallbacks.add(fallback_query + "@number")
        elif "boolean" in p_lower:
            fallbacks.add(fallback_query + "@boolean")
        elif parent_type in ["identifier", "simple_identifier"]:
            fallbacks.add(fallback_query + "@variable")

    # ==========================================
    # 阶段 4: 写入 highlights.scm
    # ==========================================
    try:
        with open(output_file, 'w', encoding='ascii') as f:
            f.write("; Automatically generated by Advanced Tree-sitter SCM Generator\n")
            f.write("; @author android_zero\n\n")

            # 1. 写入结构化查询
            f.write("; ======================================\n")
            f.write("; Structural Contexts (High Priority)\n")
            f.write("; ======================================\n\n")
            
            for tag in sorted(structured_queries.keys()):
                f.write(f"; --- {tag.upper()} ---\n")
                for query in sorted(list(structured_queries[tag])):
                    f.write(f"{query}\n")
                f.write("\n")

            # 2. 写入兜底查询 (Literals, Variables)
            f.write("; ======================================\n")
            f.write("; Literals and Fallbacks\n")
            f.write("; ======================================\n\n")
            for q in sorted(list(fallbacks)):
                f.write(f"{q}\n")
            f.write("\n")

            # 辅助函数写入数组宏
            def write_array(items, capture, title):
                if not items: return
                f.write(f"; {title}\n")
                f.write("[\n")
                for item in sorted(list(items)):
                    f.write(f'  "{escape_string(item)}"\n')
                f.write(f"] @{capture}\n\n")

            # 3. 写入无名节点
            f.write("; ======================================\n")
            f.write("; Keywords, Operators & Punctuation\n")
            f.write("; ======================================\n\n")
            write_array(unnamed_keywords, "keyword", "Keywords")
            write_array(unnamed_operators, "operator", "Operators")
            write_array(unnamed_brackets, "punctuation.bracket", "Brackets")
            write_array(unnamed_delimiters, "punctuation.delimiter", "Delimiters")

        print(f"\nSuccess! Highly accurate SCM file generated at: {output_file}")

    except Exception as e:
        print(f"Failed to write file: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Generate structural highlights.scm from node-types.json")
    parser.add_argument("-i", "--input", required=True, help="Path to src/node-types.json")
    parser.add_argument("-o", "--output", required=True, help="Path to output highlights.scm")

    args = parser.parse_args()
    generate_scm(args.input, args.output)