Tree-sitter 高亮映射权威文档

如果你需要手动理解和配置，以下是关于如何提取和映射的规范文档。

AndroidIDE Tree-sitter 语法高亮映射指南

@author android_zero
本文档定义了如何从 Tree-sitter .scm (Scheme) 文件中提取捕获节点，并将其正确映射到 AndroidIDE 的 JSON 颜色系统中。

1. 核心原理

Tree-sitter 引擎通过 highlights.scm 文件来查询抽象语法树 (AST)。当查询匹配时，会使用 @ 符号（例如 @keyword）为该语法片段打上“标签（Capture Name）”。
AndroidIDE 的 [Language].json 文件的核心作用，就是将这些“标签”与 default.json 中定义的“十六进制颜色变量”绑定起来。

2. 提取规则规范
2.1 识别目标

在 .scm 文件中，寻找所有紧跟在括号 ) 或中括号 ] 之后的以 @ 开头的字符串。

有效提取：(string) @string -> 提取为 string。

有效提取：[ "+" "-" ] @operator -> 提取为 operator。

2.2 需要忽略的标识符

部分以 @ 开头的字符并不是用于颜色高亮的，必须在生成 JSON 时忽略：

断言指令：以 ? 结尾。例如 @match?、@eq?。

代码注入：以 injection. 开头。例如 @injection.language、@injection.content。这些由编辑器内部引擎处理跨语言嵌套。

局部变量追踪：以 local. 开头。例如 @local.definition。这些在 locals.scm 中使用，由 IDE 自动提供引用高亮。

3. 智能键值映射策略 (Intelligent Key Mapping)

Tree-sitter 社区有一套约定俗成的命名规范（例如 type.builtin，点号表示层级）。在为 JSON 绑定颜色时，应该利用 default.json 的全局变量。

以下是标准映射字典（遇到相应的 @ 标签时，应赋予对应的属性）：

SCM Capture Name	对应 JSON Style 配置	视觉效果说明
keyword, keyword.*	{"fg": "@keyword", "bold": true}	红色加粗，表示控制流、修饰符等。
type, type.*	{"fg": "@type", "bold": true}	蓝色加粗，表示类、结构体、接口等。
function, method	{"fg": "@func.decl", "bold": true}	浅蓝加粗，表示函数/方法声明。
function.call	{"fg": "@func.call"}	浅蓝色，表示函数调用。
variable	{"fg": "@onSurface"}	主题默认文本色（如深灰/白），表示普通变量。
property, field	{"fg": "@field"}	橙色，表示对象成员或键。
string	{"fg": "@string"}	绿色，表示字符串字面量。
number	{"fg": "@number"}	绿色，表示数字字面量。
constant, boolean	{"fg": "@constant", "bold": true}	橙色加粗，表示布尔值或常量。
operator	{"fg": "@operator"}	蓝色，表示 +, -, =, && 等操作符。
punctuation.bracket	{"fg": "@outline"}	灰色，表示 (), `

### 如何制作完善json高亮映射体系

如果你想手动或系统性地管理高亮，请遵循以下三个步骤：

#### 第一步：从 `.scm` 文件中提取 Capture Name
Tree-sitter 的高亮查询（Queries）是通过 `@` 符号标识捕获组的。
*   **方法**：打开 `highlights.scm`，搜索 `@` 字符。
*   **示例**：`(function_definition name: (identifier) @function)` -> 提取出 `@function`。
*   **注意**：不要提取 `(#match? ...)` 或其他谓词中的 `@`。

#### 第二步：在 `default.json` 中定义颜色变量
不要在每个 `xxLang.json` 中硬编码颜色值。始终在 `default.json` 的 `definitions` 块中定义颜色变量，这样可以保证全局主题的一致性。

```json
"definitions": {
    "my.custom.color": "#FF5722"
}
```

#### 第三步：建立映射 (`xxLang.json`)
在具体的语言配置文件中，将提取到的 `@capture` 映射到 `default.json` 定义的变量：

*   **简单映射**：直接引用变量。
    ```json
    "styles": {
        "function": "@func.decl"
    }
    ```
*   **高级映射**：添加样式修饰（如加粗、斜体）。
    ```json
    "styles": {
        "keyword": { "fg": "@keyword", "bold": true }
    }
    ```

### 最佳实践建议：
1.  **保持语义命名**：在 `highlights.scm` 中，尽量使用 Tree-sitter 的标准命名规范（例如：`@variable.parameter`, `@function.builtin`），这样在跨语言开发时，所有语言的颜色风格是自动统一的。
2.  **模块化 SCM**：如果你的 SCM 文件太大，建议将其拆分为 `highlights.scm` (颜色)、`locals.scm` (变量作用域)、`tags.scm` (符号定义) 三个文件，并在 AndroidIDE 的代码中分别读取。
3.  **调试技巧**：如果某个语法高亮不生效，首先检查该节点在 `Grammar.js` 中是否真的存在，其次检查对应的 SCM 文件是否被 `TreeSitterLanguageSpec` 正确加载。


