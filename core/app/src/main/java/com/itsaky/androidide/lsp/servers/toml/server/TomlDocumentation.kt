package com.itsaky.androidide.lsp.servers.toml.server

import org.eclipse.lsp4j.*
import org.toml.lang.lexer._TomlLexer
import org.toml.lang.psi.TomlElementTypes
import java.io.StringReader

/**
 * 静态文档数据源
 */
object TomlDocumentation {
    val SCHEMA_DOCS = mapOf(
        // === 1. Keys (Semantics) ===
        "bare-key" to "Bare keys. Only allow A-Z, a-z, 0-9, underscores (_), and dashes (-).",
        "quoted-key" to "Quoted keys. Allow any Unicode character. Follow the same escape rules as basic/literal strings.",
        "dotted-key" to "Dotted keys. Define nested paths by connecting keys with dots (e.g., a.b.c = 1), equivalent to defining table structures.",

        // === 2. Strings (Lexical) ===
        "string-basic" to "Basic strings. Surrounded by double quotes; support escape sequences (e.g., \\n, \\r, \\t, \\uXXXX, \\UXXXXXXXX).",
        "string-multiline" to "Multi-line basic strings. Surrounded by three double quotes \"\"\". Newlines after the opening delimiter are ignored.",
        "string-multiline-clipping" to "Multi-line string backslash. A \\ at the end of a line trims the newline and all preceding whitespace until the next non-whitespace character.",
        "string-literal" to "Literal strings. Surrounded by single quotes. No escaping supported; content is preserved exactly (ideal for Regex or paths).",
        "string-literal-multiline" to "Multi-line literal strings. Surrounded by three single quotes '''. No escaping supported.",

        // === 3. Numbers (Semantics) ===
        "integer-decimal" to "Decimal integers. Support +/- prefixes. Underscores (_) allowed as visual separators (e.g., 1_000).",
        "integer-hex" to "Hexadecimal integers. Start with 0x; support A-F (case-insensitive); allow underscores.",
        "integer-octal" to "Octal integers. Start with 0o; allow underscores.",
        "integer-binary" to "Binary integers. Start with 0b; allow underscores.",
        "float-simple" to "Floating-point numbers. Consist of integer, fractional, and/or exponent parts. Support underscores.",
        "float-special" to "Special floats. Includes infinity (inf, +inf, -inf) and Not-a-Number (nan, +nan, -nan).",

        // === 4. Booleans & Datetimes ===
        "boolean" to "Booleans. Must be lowercase 'true' or 'false'.",
        "offset-datetime" to "Offset Date-Time. Follows RFC 3339 (e.g., 1979-05-27T07:32:00Z or 07:32:00-08:00).",
        "local-datetime" to "Local Date-Time. Omits the time zone offset; represents a naive local time.",
        "local-date" to "Local Date. Contains only the date part (YYYY-MM-DD).",
        "local-time" to "Local Time. Contains only the time part (HH:MM:SS or HH:MM:SS.f).",

        // === 5. Collections ===
        "array" to "Arrays. Collections of values in square brackets. Support multi-line, trailing commas, and mixed types (since TOML v1.0.0).",
        "table" to "Tables (Hash Tables/Dictionaries). Define a named namespace. Table names cannot be redefined unless appending to an array of tables.",
        "inline-table" to "Inline Tables. Compact table syntax { k = v }. Must be on a single line; no trailing commas allowed.",
        "array-of-tables" to "Array of Tables. Defined with double brackets [[name]]. Each occurrence creates a new table element in the array.",

        // === 6. Comments & Whitespace ===
        "comment" to "Comments. Start with # and continue to the end of the line. Cannot be placed inside key-value pairs.",
        "whitespace" to "Whitespace. TOML only supports Tab (0x09) and Space (0x20) as horizontal whitespace.",

        // === 7. Cargo / Package Specifics ===
        "package" to "Root node for project configuration. Defines metadata for the Rust project.",
        "dependencies" to "Core dependencies. Libraries required for the project to run.",
        "dev-dependencies" to "Development dependencies. Required only for tests, examples, or benchmarks.",
        "build-dependencies" to "Build dependencies. Libraries used specifically for the build.rs script.",
        "features" to "Conditional compilation. Defines optional feature sets and their associated dependencies.",
        "workspace" to "Workspace mode. Manages multiple related crates within a single repository.",
        "profile" to "Compiler profiles. Defines optimization levels, LTO, and panic strategies for dev/release.",
        "target" to "Platform-specific dependencies. Configures dependencies for specific OS or architectures.",
        "patch" to "Dependency patching. Overrides dependency sources globally, often used for upstream bug fixes.",
        "lib" to "Library target configuration. Controls attributes of the generated .rlib or .so.",
        "bin" to "Binary targets. Defines the entry point and name for executable files.",
        "metadata" to "Custom metadata. Allows tools to store private configurations ignored by Cargo.",

        // === 8. Extended TOML / Semantic Nuances (NEW) ===
        "key-value-pair" to "Key-Value Pair. The fundamental building block (key = value). There must be at most one key-value pair per line.",
        "indentation" to "Indentation. TOML ignores indentation in tables and arrays; it is purely for human readability.",
        "newline-char" to "Newline characters. TOML uses LF (0x0A) or CRLF (0x0D 0x0A) as line endings.",
        "duplicate-keys" to "Redefinition. Redefining a key or a static table is an error, maintaining data integrity.",
        "key-nesting-auto-create" to "Implicit Table Creation. Defining 'a.b.c = 1' automatically creates tables 'a' and 'b' if they don't exist.",
        "unicode-escape" to "Unicode Escapes. \\uXXXX (4 hex digits) or \\UXXXXXXXX (8 hex digits) for representing any Unicode character in basic strings."
    )
}


/**
 * Hover 提供者
 */
object TomlHoverProvider {
    fun compute(content: String, position: Position): Hover? {
        // 简单实现：找到光标下的单词，查表返回文档
        val word = findWordAt(content, position) ?: return null
        val doc = TomlDocumentation.SCHEMA_DOCS[word] ?: return null
        
        return Hover(MarkupContent("markdown", "**$word**\n\n$doc"))
    }

    private fun findWordAt(content: String, position: Position): String? {
        val lexer = _TomlLexer(StringReader(content))
        lexer.reset(content, 0, content.length, _TomlLexer.YYINITIAL)
        
        // 转换行列为 offset
        // 注意：这里为了性能简单遍历，生产环境应缓存 LineOffsets
        var currentLine = 0
        var currentCol = 0
        var targetOffset = -1
        
        for (i in content.indices) {
            if (currentLine == position.line && currentCol == position.character) {
                targetOffset = i
                break
            }
            if (content[i] == '\n') {
                currentLine++
                currentCol = 0
            } else {
                currentCol++
            }
        }
        
        if (targetOffset == -1 && currentLine == position.line) targetOffset = content.length // End of file case

        while (true) {
            val type = try { lexer.advance() } catch(e:Exception) { null } ?: break
            if (targetOffset >= lexer.tokenStart && targetOffset <= lexer.tokenEnd) {
                if (type == TomlElementTypes.BARE_KEY || type == TomlElementTypes.KEY) {
                    val start = lexer.tokenStart
                    val end = lexer.tokenEnd
                    return content.substring(start, end)
                }
            }
            if (lexer.tokenStart > targetOffset) break
        }
        return null
    }
}

/**
 * 补全提供者
 */
object TomlCompletionProvider {
    fun compute(content: String, position: Position): List<CompletionItem> {
        val items = ArrayList<CompletionItem>()
        
        // 1. 基础关键字
        items.add(makeItem("true", CompletionItemKind.Keyword, "Boolean true"))
        items.add(makeItem("false", CompletionItemKind.Keyword, "Boolean false"))
        
        // 2. 常见 Schema Keys
        TomlDocumentation.SCHEMA_DOCS.keys.forEach { key ->
            items.add(makeItem(key, CompletionItemKind.Property, "Schema Key"))
        }

        // 3. Snippets
        val tableSnippet = CompletionItem("New Table").apply {
            kind = CompletionItemKind.Snippet
            insertText = "[\${1:table_name}]\n\$0"
            insertTextFormat = InsertTextFormat.Snippet
            detail = "Table Definition"
        }
        items.add(tableSnippet)

        return items
    }
    
    private fun makeItem(label: String, kind: CompletionItemKind, detail: String): CompletionItem {
        return CompletionItem(label).apply {
            this.kind = kind
            this.detail = detail
        }
    }
}