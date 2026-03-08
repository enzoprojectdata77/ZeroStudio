(interface_declaration name: (identifier) @type)
(parcelable_declaration name: (identifier) @type)

(type_identifier) @type
(scoped_type_identifier) @type
(generic_type) @type

(boolean_type) @type.builtin
(void_type) @type.builtin
(integral_type) @type.builtin
(floating_point_type) @type.builtin

(array_type (_) @type)

(method_declaration name: (identifier) @function)

(field_declaration (variable_declarator (identifier) @property))

(element_value_pair key: (identifier) @property)
(field_access field: (identifier) @property)

(formal_parameter (identifier) @variable.parameter)

(local_variable_declaration (variable_declarator (identifier) @variable))

(assignment_expression left: (identifier) @variable)
(single_static_import member: (identifier) @variable)

(package_declaration name: (identifier) @namespace)
(package_declaration name: (scoped_identifier) @namespace)

(identifier) @variable

(true) @constant.builtin
(false) @constant.builtin
(null_literal) @constant.builtin

(decimal_integer_literal) @number
(hex_integer_literal) @number
(octal_integer_literal) @number
(binary_integer_literal) @number
(decimal_floating_point_literal) @number
(hex_floating_point_literal) @number

(string_literal) @string
(character_literal) @string
(escape_sequence) @string.escape

(line_comment) @comment
(block_comment) @comment

(annotation name: (identifier) @attribute)
(annotation name: (scoped_identifier) @attribute)
(marker_annotation name: (identifier) @attribute)
(marker_annotation name: (scoped_identifier) @attribute)


[
  "class" "interface" "parcelable" "extends"
  "import" "package" "assert" "default" 
  "return" "yield" "throws" "static"
  "oneway" "in" "out" "inout"
  "byte" "short" "int" "long" "char" "float" "double"
] @keyword[
  "!=" "%" "&" "&&" "*" "+" "-" "/" "<" "<<" "<=" 
  "=" "==" ">" ">=" ">>" ">>>" "?" "^" "|" "||"
] @operator

[
  "(" ")" "[" "]" "{" "}"
] @punctuation.bracket

[
  "," ":" ";" "." "..."
] @punctuation.delimiter

"@" @punctuation.special