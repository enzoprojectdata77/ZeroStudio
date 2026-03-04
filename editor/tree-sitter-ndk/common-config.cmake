# 获取当前文件（common-config.cmake）所在的目录路径
# 对应: root/ZeroStudio-ZeroStudio-devs/editor/tree-sitter-ndk/
set(NDK_ROOT_DIR "${CMAKE_CURRENT_LIST_DIR}")

# 定义 tree-sitter-lib 的根目录
if (NOT DEFINED TS_DIR)
    set(TS_DIR "${NDK_ROOT_DIR}/tree-sitter-lib")
endif ()

# 打印日志帮助调试路径问题
message(STATUS "Tree-Sitter Lib Dir: ${TS_DIR}")

# 包含 tree-sitter 核心头文件
# lib/include 包含 <tree_sitter/api.h>
# lib/src 包含 parser.h 等内部实现头文件
set(TS_INCLUDES 
    "${TS_DIR}/lib/include" 
    "${TS_DIR}/lib/src"
)

# 将这些目录添加到编译器的搜索路径中
include_directories(${TS_INCLUDES})

# 针对非 Android 环境（如 Host 单元测试）查找 JNI
if (NOT ${CMAKE_SYSTEM_NAME} STREQUAL Android)
    find_package(JNI REQUIRED)
    include_directories(${JAVA_INCLUDE_PATH})
    include_directories(${JAVA_INCLUDE_PATH2})
endif ()