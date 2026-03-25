; --------------------------------------
; Tables & Arrays of Tables
; --------------------------------------

(table
  (bare_key) @type)

(table
  (dotted_key
    (bare_key) @type)
)

(table
  (quoted_key) @type)

(table_array_element
  (bare_key) @type)

(table_array_element
  (dotted_key
    (bare_key) @type)
)

(table_array_element
  (quoted_key) @type)

; --------------------------------------
; Keys (Properties)
; --------------------------------------

; Standard key = value
(pair
  (bare_key) @field)

; Dotted keys: a.b.c = value
(pair
  (dotted_key
    (bare_key) @field)
)

; Inline table keys: { key = val }
(inline_table
  (pair
    (bare_key) @field))

(inline_table
  (pair
    (dotted_key
      (bare_key) @field))
)

; Quoted keys
(pair
  (quoted_key) @string)

; --------------------------------------
; Values & Literals
; --------------------------------------

(string) @string

(escape_sequence) @string.escape

[
  (integer)
  (float)
] @number

(boolean) @keyword

[
  (offset_date_time)
  (local_date_time)
  (local_date)
  (local_time)
] @constant

; --------------------------------------
; Punctuation & Delimiters
; --------------------------------------

(comment) @comment

"=" @operator

[
  "."
  ","
] @punctuation.delimiter

[
  "["
  "]"
  "[["
  "]]"
  "{"
  "}"
] @punctuation.bracket