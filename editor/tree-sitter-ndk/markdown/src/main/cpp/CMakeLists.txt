cmake_minimum_required(VERSION 3.22.1)

project("tree-sitter-markdown")

get_filename_component(NDK_COMMON_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../../../.." ABSOLUTE)

include("${NDK_COMMON_DIR}/common-config.cmake")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include_directories(${CMAKE_CURRENT_SOURCE_DIR})

# 包含两个解析器的 src 目录
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/tree-sitter-markdown/src)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/tree-sitter-markdown-inline/src)

add_library(${CMAKE_PROJECT_NAME} SHARED
        "${TS_DIR}/lib/src/lib.c"
        
        # Markdown Block Parser
        tree-sitter-markdown/src/parser.c
        tree-sitter-markdown/src/scanner.c
        
        # Markdown Inline Parser
        tree-sitter-markdown-inline/src/parser.c
        tree-sitter-markdown-inline/src/scanner.c
        
        # JNI Bridge
        tree-sitter-markdown.cpp
)

if (${CMAKE_SYSTEM_NAME} STREQUAL Android)
    find_library(log-lib log)
    target_link_libraries(${CMAKE_PROJECT_NAME} ${log-lib})
endif()