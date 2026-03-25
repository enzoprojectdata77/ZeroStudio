cmake_minimum_required(VERSION 3.22.1)

project("tree-sitter-reStructuredText")

# 从 src/main/cpp (当前目录) 向上跳 4 级到达 tree-sitter-ndk 目录
get_filename_component(NDK_COMMON_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../../../.." ABSOLUTE)

# 引入通用配置
include("${NDK_COMMON_DIR}/common-config.cmake")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# 包含头文件目录
include_directories(${CMAKE_CURRENT_SOURCE_DIR})
# 包含 tree_sitter_rst 目录，以便 scanner.c 能找到内部的 .h 和 .c 文件
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/tree_sitter_rst)

add_library(${CMAKE_PROJECT_NAME} SHARED
        "${TS_DIR}/lib/src/lib.c"
        parser.c
        scanner.c
        tree-sitter-reStructuredText.cpp
)

# 链接 Android 日志库
if (${CMAKE_SYSTEM_NAME} STREQUAL Android)
    find_library(log-lib log)
    target_link_libraries(${CMAKE_PROJECT_NAME} ${log-lib})
endif()