#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Tree-sitter Scheme Generator for AndroidIDE
@author android_zero

This script automatically extracts '@capture' tags from Tree-sitter query files (*.scm),
intelligently maps them to AndroidIDE's color variables based on semantic naming conventions,
and generates a compliant JSON configuration file.
"""

import os
import re
import json
import argparse
from typing import Set, Dict, Any

# 预设的智能颜色映射表 (AndroidIDE 规范)
# 键为 Tree-sitter 标准的 capture 节点名，值为对应的高亮样式对象
SMART_COLOR_MAP: Dict[str, Dict[str, Any]] = {
    # Keywords
    "keyword": {"fg": "@keyword", "bold": True},
    "keyword.control": {"fg": "@keyword", "bold": True},
    "keyword.directive": {"fg": "@kt.preproc", "bold": True},
    "keyword.modifier": {"fg": "@keyword", "italic": True},
    "keyword.visibility": {"fg": "@keyword", "bold": True},
    # Types & Classes
    "type": {"fg": "@type", "bold": True},
    "type.builtin": {"fg": "@keyword", "bold": True},
    "constructor": {"fg": "@type", "bold": True},
    "namespace": {"fg": "@java.package"},
    # Variables & Properties
    "variable": {"fg": "@onSurface"},
    "variable.parameter": {"fg": "@variable"},
    "variable.builtin": {"fg": "@keyword", "bold": True},
    "property": {"fg": "@field"},
    "field": {"fg": "@field"},
    "attribute": {"fg": "@attribute", "italic": True},
    # Functions & Methods
    "function": {"fg": "@func.decl", "bold": True},
    "function.call": {"fg": "@func.call"},
    "function.method": {"fg": "@func.decl", "italic": True},
    "function.method.call": {"fg": "@func.call", "italic": True},
    "function.builtin": {"fg": "@func.call", "bold": True, "italic": True},
    "function.macro": {"fg": "@func.call", "bold": True},
    # Literals
    "string": {"fg": "@string"},
    "string.special": {"fg": "@string", "bold": True},
    "string.escape": {"fg": "@func.decl", "bold": True},
    "number": {"fg": "@number"},
    "boolean": {"fg": "@keyword", "bold": True},
    "constant": {"fg": "@constant"},
    "constant.builtin": {"fg": "@keyword", "bold": True},
    "constant.macro": {"fg": "@constant", "bold": True},
    # Comments
    "comment": {"fg": "@comment", "italic": True},
    # Punctuation & Operators
    "operator": {"fg": "@operator"},
    "punctuation.delimiter": {"fg": "@onSurfaceVariant"},
    "punctuation.bracket": {"fg": "@outline"},
    "punctuation.special": {"fg": "@kt.punctuation.special"},
    # Special / Fallbacks
    "label": {"fg": "@func.decl"},
    "embedded": {"fg": "@onSurface"}
}

def extract_captures_from_scm(file_path: str) -> Set[str]:
    """
    Extracts all unique '@capture' strings from a given .scm file.
    
    :param file_path: The path to the .scm file.
    :return: A set of unique capture names (without the '@' prefix).
    """
    captures = set()
    capture_pattern = re.compile(r'@([\w.-]+)')
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            # Remove line comments to prevent extracting from commented-out queries
            content = re.sub(r';.*', '', content)
            matches = capture_pattern.findall(content)
            for match in matches:
                # Exclude internal parser directives/predicates like match?, eq?
                if not match.endswith('?'):
                    captures.add(match)
    except Exception as e:
        print(f"Error reading file {file_path}: {e}")
        
    return captures

def resolve_style_for_capture(capture: str) -> Dict[str, Any]:
    """
    Resolves the best matching style for a given capture name using fallback logic.
    For example, if 'string.regexp' is not found, it falls back to 'string'.
    
    :param capture: The capture name to resolve.
    :return: A dictionary representing the style.
    """
    parts = capture.split('.')
    
    # Try to find the exact match or progressive fallback
    # e.g., 'variable.parameter.builtin' -> 'variable.parameter' -> 'variable'
    while parts:
        key = '.'.join(parts)
        if key in SMART_COLOR_MAP:
            return SMART_COLOR_MAP[key]
        parts.pop()
        
    # Default fallback for unknown captures
    return {"fg": "@onSurface"}

def generate_json_config(language_name: str, file_extensions: list, captures: Set[str], output_path: str):
    """
    Generates the AndroidIDE JSON highlighting configuration.
    
    :param language_name: The name of the language (e.g., 'TypeScript').
    :param file_extensions: List of file extensions (e.g.,['ts', 'tsx']).
    :param captures: Set of extracted capture names.
    :param output_path: Destination path for the JSON file.
    """
    styles = {}
    
    # Sort captures alphabetically for a clean JSON output
    for capture in sorted(list(captures)):
        # Ignore injection-related captures as they are handled by the engine natively
        if capture.startswith("injection.") or capture.startswith("local."):
            continue
        styles[capture] = resolve_style_for_capture(capture)
        
    config = {
        "types": file_extensions,
        "styles": styles
    }
    
    try:
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(config, f, indent=2, ensure_ascii=False)
        print(f"Successfully generated config for {language_name} at: {output_path}")
        print(f"Extracted and mapped {len(styles)} capture nodes.")
    except Exception as e:
        print(f"Error writing JSON config: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="AndroidIDE Tree-sitter JSON Config Generator")
    parser.add_argument("-i", "--input", required=True, help="Path to the highlights.scm file")
    parser.add_argument("-o", "--output", required=True, help="Path to save the generated JSON file")
    parser.add_argument("-l", "--lang", required=True, help="Language name (e.g., Python)")
    parser.add_argument("-e", "--ext", required=True, nargs='+', help="File extensions (e.g., py pyi)")
    
    args = parser.parse_args()
    
    if not os.path.exists(args.input):
        print(f"Input file not found: {args.input}")
        exit(1)
        
    extracted_captures = extract_captures_from_scm(args.input)
    generate_json_config(args.lang, args.ext, extracted_captures, args.output)