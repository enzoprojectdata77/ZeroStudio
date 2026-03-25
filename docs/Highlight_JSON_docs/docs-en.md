#Tree-sitter Highlight Mapping Authoritative Documentation

+ If you need to understand and configure it manually, here is the specification document on how to extract and map.

## AndroidIDE Tree-sitter Syntax highlighting mapping guide

; @author android_zero
This document defines how to extract capture nodes from a Tree-sitter .scm (Scheme) file and correctly map them to the JSON color system of the Android IDE.

+ 1. Core Principles

The Tree-sitter engine queries the Abstract Syntax Tree (AST) through the highlights.scm file. When a match is found, the syntax fragment is tagged with an @ symbol (e.g., @keyword) as a "capture name".

The core function of the Android IDE's [Language.json] file is to bind these tags to the hexadecimal color variables defined in default.json.

# 2. Extraction Rule Specification

+ 2.1 Target Identification

In the .scm file, find all strings that begin with @ immediately following a parenthesis ) or square bracket ].

Valid extraction: (string) @string -> extracts as string.

Valid extraction: [ "+" "-" ] @operator -> extracts as operator.

+ 2.2Identifiers to be Ignored

Some characters starting with @ are not used for color highlighting and must be ignored when generating JSON:

Assertion directives: End with ?. For example, @match?, @eq?.

Code injection: Begin with injection. For example, @injection.language, @injection.content. These are handled by the editor's internal engine for cross-language nesting.

Local variable tracking: Begins with local. For example, @local.definition. These are used in locals.scm and are automatically highlighted by the IDE.

3. Intelligent Key Mapping Strategy

The Tree-sitter community has a set of established naming conventions (e.g., type.builtin, with dots indicating hierarchy). When binding colors to JSON, global variables in default.json should be used.

The following is a standard mapping dictionary (the corresponding attributes should be assigned when encountering the corresponding @ tag):

SCM Capture Name	Visual effects description for corresponding JSON Style configuration
keyword, keyword.*	{"fg": "@keyword", "bold": true}	The text in bold red indicates control flow, modifiers, etc.
type, type.*	{"fg": "@type", "bold": true}	The text in bold blue indicates classes, structs, interfaces, etc.
function, method	{"fg": "@func.decl", "bold": true}	Light blue bold indicates a function/method declaration.
function.call	{"fg": "@func.call"}	Light blue indicates a function call.
variable	{"fg": "@onSurface"}	The theme's default text color (such as dark gray/white) represents ordinary variables.
property, field	{"fg": "@field"}	Orange indicates object members or keys.
string	{"fg": "@string"}	Green indicates a string literal.
number	{"fg": "@number"}	Green indicates a numerical literal.
constant, boolean	{"fg": "@constant", "bold": true}	Orange bold indicates Boolean values ​​or constants.
operator	{"fg": "@operator"}	Blue indicates +, -, =, && operators.
punctuation.bracket	{"fg": "@outline"}	Gray indicates (), `

### How to Create a Complete JSON Highlighting Mapping System

If you want to manage highlighting manually or systematically, please follow these three steps:

#### Step 1: Extract Capture Name from the `.scm` file

Tree-sitter highlight queries are identified by the `@` symbol.

* **Method**: Open `highlights.scm` and search for the `@` character.

* **Example**: `(function_definition name: (identifier) ​​@function)` -> Extracts `@function`.

* **Note**: Do not extract `@` from `(#match? ...)` or other predicates.

#### Step 2: Define color variables in `default.json`

Do not hardcode color values ​​in each `xxLang.json` file. Always define color variables in the `definitions` block of `default.json` to ensure consistency of the global theme.

```json
"definitions": {
    "my.custom.color": "#FF5722"
}
```

#### Step 3: Establish the mapping (`xxLang.json`)
In the specific language configuration file, map the extracted `@capture` to the variables defined in `default.json`:

* **Simple mapping**: Directly referencing variables.
    ```json
    "styles": {
        "function": "@func.decl"
    }
    ```
**Advanced Mapping:** Add style embellishments (such as bold, italics).
    ```json
    "styles": {
        "keyword": { 
        "fg": "@keyword",
         "bold": true 
         }
    }
    ```

### Best Practices:

1. **Maintain Semantic Naming:** In `highlights.scm`, try to use the standard naming conventions of Tree-sitter (e.g., `@variable.parameter`, `@function.builtin`). This ensures that the color style is automatically consistent across all languages ​​during cross-language development.

2. **Modify SCM:** If your SCM file is too large, it is recommended to split it into three files: `highlights.scm` (colors), `locals.scm` (variable scope), and `tags.scm` (symbol definitions), and read them separately in the Android IDE code.

3. **Debugging Techniques:** If a syntax highlighting is not working, first check if the node actually exists in `Grammar.js`, and then check if the corresponding SCM file has been correctly loaded by `TreeSitterLanguageSpec`.