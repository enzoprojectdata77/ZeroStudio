# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在使用本仓库中的代码时提供指导。

## 项目概述

RikkaHub 是一款原生 Android LLM 聊天客户端，支持在不同的 AI 提供商之间切换进行对话。

它使用 Jetpack Compose 和 Kotlin 构建，并遵循 Material Design 3 设计原则。

## 架构概述

### 模块结构

- **app**：包含 UI、ViewModel 和核心逻辑的主应用程序模块

- **ai**：适用于不同提供商（OpenAI、Google、Anthropic）的 AI SDK 抽象层

- **common**：通用工具和扩展

- **document**：用于处理 PDF、DOCX 和 PPTX 文件的文档解析模块

- **highlight**：代码语法高亮实现

- **search**：搜索功能 SDK（Exa、Tavily、Zhipu）

- **tts**：适用于不同提供商的文本转语音实现

- **web**：嵌入式 Web 服务器模块，提供 Ktor 服务器启动功能并托管静态前端构建文件（

基于 web-ui/React 项目构建）

### 关键技术

- **Jetpack Compose**：现代 UI 工具包

- **Koin**：依赖注入

- **Room**：数据库 ORM

- **DataStore**：偏好设置存储

- **OkHttp**：支持 SSE 的 HTTP 客户端

- **导航组合**：应用导航

- **Kotlinx 序列化**：JSON 处理

### 核心包（应用模块）

- `data/`：数据层，包含存储库、数据库实体和 API 客户端

- `ui/pages/`：屏幕实现和 ViewModel

- `ui/components/`：可重用的 UI 组件

- `di/`：依赖注入模块

- `utils/`：实用函数和扩展

### 概念

- **助手**：助手配置，包含系统提示、模型参数和对话隔离。每个助手都维护自己的设置，包括温度、上下文大小、自定义标头、工具、内存选项、正则表达式转换和提示注入（模式/日志）。助手提供具有特定行为和功能的隔离聊天环境。 （app/src/main/java/me/rerere/rikkahub/data/model/Assistant.kt）

- **对话**：用户与助手之间持久的对话线程。每个对话都维护一个树状结构的 MessageNode 列表，以支持消息分支，并包含标题、创建时间和置顶状态等元数据。对话可以在特定索引处截断，并保留聊天建议。（app/src/main/java/me/rerere/rikkahub/data/model/Conversation.kt）

- **UIMessage**：一个与平台无关的消息抽象层，它封装了包含不同类型内容部分（文本、图像、文档、推理、工具调用/结果等）的聊天消息。每条消息都有一个角色（用户、助手、系统、工具）、创建时间戳、模型 ID、令牌使用信息以及可选的注解。UIMessage 支持通过块合并进行流式更新。 （ai/src/main/java/me/rerere/ai/ui/Message.kt）

- **MessageNode**：一个容器，包含一个或多个 UIMessage，用于实现消息分支功能。每个节点维护一个备选消息列表，并跟踪当前选中的消息（selectIndex）。这使得用户可以重新生成回复并在不同的对话分支之间切换，从而创建树状的对话结构。（app/src/main/java/me/rerere/rikkahub/data/model/Conversation.kt）

- **Message Transformer**：一种管道机制，用于在将消息发送给 AI 提供商（InputMessageTransformer）之前或接收回复（OutputMessageTransformer）之后转换消息。转换器可以修改消息内容、添加元数据、应用模板、处理特殊标签、转换格式以及执行 OCR。常用转换器包括：

- TemplateTransformer：将 Pebble 模板应用于包含时间/日期等变量的用户消息

- ThinkTagTransformer：提取 `<think>` 标签并将其转换为推理部分

- RegexOutputTransformer：将正则表达式替换应用于助手回复

- DocumentAsPromptTransformer：将文档附件转换为文本提示

- Base64ImageToLocalFileTransformer：将 Base64 图像转换为本地文件引用

- OcrTransformer：对图像执行 OCR 以提取文本

输出转换器支持 `visualTransform()` 用于流式传输期间的 UI 显示，以及 `onGenerationFinish()` 用于生成完成后的最终处理。

(app/src/main/java/me/rerere/rikkahub/data/ai/transformers/Transformer.kt)

## 开发指南

### UI 开发

- 遵循 Material Design 3 原则

- 使用 `ui/components/` 目录下的现有 UI 组件

- 参考 `SettingProviderPage.kt` 中的页面布局模式

- 使用 `FormItem` 实现一致的表单布局

- 使用 ViewModel 实现正确的状态管理

- 使用 `Lucide.XXX` 作为图标库，并为每个图标导入 `import com.composables.icons.lucide.XXX`

- 使用 `LocalToaster.current` 来显示 Toast 消息

### 国际化

- 字符串资源位于 `app/src/main/res/values-*/strings.xml` 中

- 在 Compose 中使用 `stringResource(R.string.key_name)`

- 页面特定的字符串应使用页面前缀（例如，`setting_page_`）

- 如果用户没有明确请求本地化：优先实现功能，暂不考虑本地化。

（例如：`Text("Hello world")`）

- 如果用户明确请求本地化，则应支持所有语言。

- 默认语言为英语 (en)。支持中文 (zh)、日语 (ja)、繁体中文 (zh-rTW) 和韩语 (ko-rKR)。

- 需要本地化时，请使用 `locale-tui-localization` 技能来管理字符串资源。

### 数据库

- 支持迁移的 Room 数据库

- 模式文件位于 `app/schemas/` 目录下

- 使用 KSP 处理 Room 注解

- 当前数据库版本在 `AppDatabase.kt` 文件中跟踪

### AI 提供程序集成

- 新的提供程序位于 `ai/src/main/java/me/rerere/ai/provider/providers/` 目录下

- 扩展基类 `Provider`

- 按照现有模式实现所需的 API 方法

- 支持通过 SSE 流式传输响应