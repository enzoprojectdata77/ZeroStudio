#include "tree_sitter/parser.h"

#if defined(__GNUC__) || defined(__clang__)
#pragma GCC diagnostic ignored "-Wmissing-field-initializers"
#endif

#define LANGUAGE_VERSION 14
#define STATE_COUNT 144
#define LARGE_STATE_COUNT 2
#define SYMBOL_COUNT 56
#define ALIAS_COUNT 0
#define TOKEN_COUNT 28
#define EXTERNAL_TOKEN_COUNT 0
#define FIELD_COUNT 7
#define MAX_ALIAS_SEQUENCE_LENGTH 6
#define PRODUCTION_ID_COUNT 9

enum ts_symbol_identifiers {
  anon_sym_LT_QMARK = 1,
  anon_sym_QMARK_GT = 2,
  anon_sym_LT = 3,
  anon_sym_SLASH = 4,
  anon_sym_GT = 5,
  anon_sym_xmlns = 6,
  anon_sym_COLON = 7,
  aux_sym_attr_value_token1 = 8,
  anon_sym_xml = 9,
  anon_sym_version = 10,
  anon_sym_encoding = 11,
  sym_name = 12,
  anon_sym_LT_BANG_DASH_DASH = 13,
  anon_sym_DASH_DASH_GT = 14,
  sym_char_data = 15,
  sym_char_ref = 16,
  sym_cdata_start = 17,
  sym_cdata_end = 18,
  anon_sym_AMP = 19,
  anon_sym_SEMI = 20,
  sym_eq = 21,
  sym__char = 22,
  sym__ws = 23,
  anon_sym_DQUOTE = 24,
  anon_sym_SQUOTE = 25,
  sym__dec_num = 26,
  sym__encoding = 27,
  sym_xml_file = 28,
  sym_pi = 29,
  sym_element = 30,
  sym_end_tag_element = 31,
  sym_empty_element = 32,
  sym_tag_start = 33,
  sym_tag_end = 34,
  aux_sym__content = 35,
  sym_attribute = 36,
  sym_ns_decl = 37,
  sym_xml_attr = 38,
  sym_attr_value = 39,
  sym_xml_decl = 40,
  sym_xml_version = 41,
  sym_xml_version_value = 42,
  sym_xml_encoding = 43,
  sym_xml_encoding_value = 44,
  sym_comment = 45,
  sym_cdata_sect = 46,
  sym_cdata = 47,
  sym__ref = 48,
  sym_entity_ref = 49,
  sym__misc = 50,
  sym__quote = 51,
  aux_sym_xml_file_repeat1 = 52,
  aux_sym_pi_repeat1 = 53,
  aux_sym_empty_element_repeat1 = 54,
  aux_sym_attr_value_repeat1 = 55,
};

static const char * const ts_symbol_names[] = {
  [ts_builtin_sym_end] = "end",
  [anon_sym_LT_QMARK] = "<\?",
  [anon_sym_QMARK_GT] = "\?>",
  [anon_sym_LT] = "<",
  [anon_sym_SLASH] = "/",
  [anon_sym_GT] = ">",
  [anon_sym_xmlns] = "xmlns",
  [anon_sym_COLON] = ":",
  [aux_sym_attr_value_token1] = "attr_value_token1",
  [anon_sym_xml] = "xml",
  [anon_sym_version] = "version",
  [anon_sym_encoding] = "encoding",
  [sym_name] = "name",
  [anon_sym_LT_BANG_DASH_DASH] = "<!--",
  [anon_sym_DASH_DASH_GT] = "-->",
  [sym_char_data] = "char_data",
  [sym_char_ref] = "char_ref",
  [sym_cdata_start] = "cdata_start",
  [sym_cdata_end] = "cdata_end",
  [anon_sym_AMP] = "&",
  [anon_sym_SEMI] = ";",
  [sym_eq] = "eq",
  [sym__char] = "_char",
  [sym__ws] = "_ws",
  [anon_sym_DQUOTE] = "\"",
  [anon_sym_SQUOTE] = "'",
  [sym__dec_num] = "_dec_num",
  [sym__encoding] = "_encoding",
  [sym_xml_file] = "xml_file",
  [sym_pi] = "pi",
  [sym_element] = "element",
  [sym_end_tag_element] = "end_tag_element",
  [sym_empty_element] = "empty_element",
  [sym_tag_start] = "tag_start",
  [sym_tag_end] = "tag_end",
  [aux_sym__content] = "_content",
  [sym_attribute] = "attribute",
  [sym_ns_decl] = "ns_decl",
  [sym_xml_attr] = "xml_attr",
  [sym_attr_value] = "attr_value",
  [sym_xml_decl] = "xml_decl",
  [sym_xml_version] = "xml_version",
  [sym_xml_version_value] = "xml_version_value",
  [sym_xml_encoding] = "xml_encoding",
  [sym_xml_encoding_value] = "xml_encoding_value",
  [sym_comment] = "comment",
  [sym_cdata_sect] = "cdata_sect",
  [sym_cdata] = "cdata",
  [sym__ref] = "_ref",
  [sym_entity_ref] = "entity_ref",
  [sym__misc] = "_misc",
  [sym__quote] = "_quote",
  [aux_sym_xml_file_repeat1] = "xml_file_repeat1",
  [aux_sym_pi_repeat1] = "pi_repeat1",
  [aux_sym_empty_element_repeat1] = "empty_element_repeat1",
  [aux_sym_attr_value_repeat1] = "attr_value_repeat1",
};

static const TSSymbol ts_symbol_map[] = {
  [ts_builtin_sym_end] = ts_builtin_sym_end,
  [anon_sym_LT_QMARK] = anon_sym_LT_QMARK,
  [anon_sym_QMARK_GT] = anon_sym_QMARK_GT,
  [anon_sym_LT] = anon_sym_LT,
  [anon_sym_SLASH] = anon_sym_SLASH,
  [anon_sym_GT] = anon_sym_GT,
  [anon_sym_xmlns] = anon_sym_xmlns,
  [anon_sym_COLON] = anon_sym_COLON,
  [aux_sym_attr_value_token1] = aux_sym_attr_value_token1,
  [anon_sym_xml] = anon_sym_xml,
  [anon_sym_version] = anon_sym_version,
  [anon_sym_encoding] = anon_sym_encoding,
  [sym_name] = sym_name,
  [anon_sym_LT_BANG_DASH_DASH] = anon_sym_LT_BANG_DASH_DASH,
  [anon_sym_DASH_DASH_GT] = anon_sym_DASH_DASH_GT,
  [sym_char_data] = sym_char_data,
  [sym_char_ref] = sym_char_ref,
  [sym_cdata_start] = sym_cdata_start,
  [sym_cdata_end] = sym_cdata_end,
  [anon_sym_AMP] = anon_sym_AMP,
  [anon_sym_SEMI] = anon_sym_SEMI,
  [sym_eq] = sym_eq,
  [sym__char] = sym__char,
  [sym__ws] = sym__ws,
  [anon_sym_DQUOTE] = anon_sym_DQUOTE,
  [anon_sym_SQUOTE] = anon_sym_SQUOTE,
  [sym__dec_num] = sym__dec_num,
  [sym__encoding] = sym__encoding,
  [sym_xml_file] = sym_xml_file,
  [sym_pi] = sym_pi,
  [sym_element] = sym_element,
  [sym_end_tag_element] = sym_end_tag_element,
  [sym_empty_element] = sym_empty_element,
  [sym_tag_start] = sym_tag_start,
  [sym_tag_end] = sym_tag_end,
  [aux_sym__content] = aux_sym__content,
  [sym_attribute] = sym_attribute,
  [sym_ns_decl] = sym_ns_decl,
  [sym_xml_attr] = sym_xml_attr,
  [sym_attr_value] = sym_attr_value,
  [sym_xml_decl] = sym_xml_decl,
  [sym_xml_version] = sym_xml_version,
  [sym_xml_version_value] = sym_xml_version_value,
  [sym_xml_encoding] = sym_xml_encoding,
  [sym_xml_encoding_value] = sym_xml_encoding_value,
  [sym_comment] = sym_comment,
  [sym_cdata_sect] = sym_cdata_sect,
  [sym_cdata] = sym_cdata,
  [sym__ref] = sym__ref,
  [sym_entity_ref] = sym_entity_ref,
  [sym__misc] = sym__misc,
  [sym__quote] = sym__quote,
  [aux_sym_xml_file_repeat1] = aux_sym_xml_file_repeat1,
  [aux_sym_pi_repeat1] = aux_sym_pi_repeat1,
  [aux_sym_empty_element_repeat1] = aux_sym_empty_element_repeat1,
  [aux_sym_attr_value_repeat1] = aux_sym_attr_value_repeat1,
};

static const TSSymbolMetadata ts_symbol_metadata[] = {
  [ts_builtin_sym_end] = {
    .visible = false,
    .named = true,
  },
  [anon_sym_LT_QMARK] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_QMARK_GT] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_LT] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_SLASH] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_GT] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_xmlns] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_COLON] = {
    .visible = true,
    .named = false,
  },
  [aux_sym_attr_value_token1] = {
    .visible = false,
    .named = false,
  },
  [anon_sym_xml] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_version] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_encoding] = {
    .visible = true,
    .named = false,
  },
  [sym_name] = {
    .visible = true,
    .named = true,
  },
  [anon_sym_LT_BANG_DASH_DASH] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_DASH_DASH_GT] = {
    .visible = true,
    .named = false,
  },
  [sym_char_data] = {
    .visible = true,
    .named = true,
  },
  [sym_char_ref] = {
    .visible = true,
    .named = true,
  },
  [sym_cdata_start] = {
    .visible = true,
    .named = true,
  },
  [sym_cdata_end] = {
    .visible = true,
    .named = true,
  },
  [anon_sym_AMP] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_SEMI] = {
    .visible = true,
    .named = false,
  },
  [sym_eq] = {
    .visible = true,
    .named = true,
  },
  [sym__char] = {
    .visible = false,
    .named = true,
  },
  [sym__ws] = {
    .visible = false,
    .named = true,
  },
  [anon_sym_DQUOTE] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_SQUOTE] = {
    .visible = true,
    .named = false,
  },
  [sym__dec_num] = {
    .visible = false,
    .named = true,
  },
  [sym__encoding] = {
    .visible = false,
    .named = true,
  },
  [sym_xml_file] = {
    .visible = true,
    .named = true,
  },
  [sym_pi] = {
    .visible = true,
    .named = true,
  },
  [sym_element] = {
    .visible = true,
    .named = true,
  },
  [sym_end_tag_element] = {
    .visible = true,
    .named = true,
  },
  [sym_empty_element] = {
    .visible = true,
    .named = true,
  },
  [sym_tag_start] = {
    .visible = true,
    .named = true,
  },
  [sym_tag_end] = {
    .visible = true,
    .named = true,
  },
  [aux_sym__content] = {
    .visible = false,
    .named = false,
  },
  [sym_attribute] = {
    .visible = true,
    .named = true,
  },
  [sym_ns_decl] = {
    .visible = true,
    .named = true,
  },
  [sym_xml_attr] = {
    .visible = true,
    .named = true,
  },
  [sym_attr_value] = {
    .visible = true,
    .named = true,
  },
  [sym_xml_decl] = {
    .visible = true,
    .named = true,
  },
  [sym_xml_version] = {
    .visible = true,
    .named = true,
  },
  [sym_xml_version_value] = {
    .visible = true,
    .named = true,
  },
  [sym_xml_encoding] = {
    .visible = true,
    .named = true,
  },
  [sym_xml_encoding_value] = {
    .visible = true,
    .named = true,
  },
  [sym_comment] = {
    .visible = true,
    .named = true,
  },
  [sym_cdata_sect] = {
    .visible = true,
    .named = true,
  },
  [sym_cdata] = {
    .visible = true,
    .named = true,
  },
  [sym__ref] = {
    .visible = false,
    .named = true,
  },
  [sym_entity_ref] = {
    .visible = true,
    .named = true,
  },
  [sym__misc] = {
    .visible = false,
    .named = true,
  },
  [sym__quote] = {
    .visible = false,
    .named = true,
  },
  [aux_sym_xml_file_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_pi_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_empty_element_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_attr_value_repeat1] = {
    .visible = false,
    .named = false,
  },
};

enum ts_field_identifiers {
  field_attr_name = 1,
  field_decl = 2,
  field_encoding_attr = 3,
  field_ns_prefix = 4,
  field_tag_name = 5,
  field_version_attr = 6,
  field_xmlns_prefix = 7,
};

static const char * const ts_field_names[] = {
  [0] = NULL,
  [field_attr_name] = "attr_name",
  [field_decl] = "decl",
  [field_encoding_attr] = "encoding_attr",
  [field_ns_prefix] = "ns_prefix",
  [field_tag_name] = "tag_name",
  [field_version_attr] = "version_attr",
  [field_xmlns_prefix] = "xmlns_prefix",
};

static const TSFieldMapSlice ts_field_map_slices[PRODUCTION_ID_COUNT] = {
  [1] = {.index = 0, .length = 1},
  [2] = {.index = 1, .length = 1},
  [3] = {.index = 2, .length = 1},
  [4] = {.index = 3, .length = 1},
  [5] = {.index = 4, .length = 1},
  [6] = {.index = 5, .length = 1},
  [7] = {.index = 6, .length = 1},
  [8] = {.index = 7, .length = 2},
};

static const TSFieldMapEntry ts_field_map_entries[] = {
  [0] =
    {field_tag_name, 1},
  [1] =
    {field_decl, 1},
  [2] =
    {field_tag_name, 2},
  [3] =
    {field_version_attr, 1},
  [4] =
    {field_attr_name, 0},
  [5] =
    {field_encoding_attr, 1},
  [6] =
    {field_xmlns_prefix, 2},
  [7] =
    {field_attr_name, 2},
    {field_ns_prefix, 0},
};

static const TSSymbol ts_alias_sequences[PRODUCTION_ID_COUNT][MAX_ALIAS_SEQUENCE_LENGTH] = {
  [0] = {0},
};

static const uint16_t ts_non_terminal_alias_map[] = {
  0,
};

static const TSStateId ts_primary_state_ids[STATE_COUNT] = {
  [0] = 0,
  [1] = 1,
  [2] = 2,
  [3] = 2,
  [4] = 4,
  [5] = 5,
  [6] = 6,
  [7] = 4,
  [8] = 5,
  [9] = 6,
  [10] = 10,
  [11] = 11,
  [12] = 12,
  [13] = 13,
  [14] = 14,
  [15] = 15,
  [16] = 16,
  [17] = 17,
  [18] = 18,
  [19] = 19,
  [20] = 20,
  [21] = 21,
  [22] = 22,
  [23] = 23,
  [24] = 24,
  [25] = 25,
  [26] = 26,
  [27] = 27,
  [28] = 28,
  [29] = 29,
  [30] = 30,
  [31] = 31,
  [32] = 32,
  [33] = 33,
  [34] = 34,
  [35] = 35,
  [36] = 36,
  [37] = 37,
  [38] = 38,
  [39] = 39,
  [40] = 40,
  [41] = 26,
  [42] = 42,
  [43] = 40,
  [44] = 44,
  [45] = 45,
  [46] = 46,
  [47] = 42,
  [48] = 36,
  [49] = 25,
  [50] = 28,
  [51] = 20,
  [52] = 30,
  [53] = 31,
  [54] = 54,
  [55] = 33,
  [56] = 56,
  [57] = 19,
  [58] = 35,
  [59] = 59,
  [60] = 37,
  [61] = 38,
  [62] = 62,
  [63] = 63,
  [64] = 22,
  [65] = 29,
  [66] = 27,
  [67] = 67,
  [68] = 68,
  [69] = 69,
  [70] = 70,
  [71] = 71,
  [72] = 72,
  [73] = 73,
  [74] = 74,
  [75] = 75,
  [76] = 76,
  [77] = 71,
  [78] = 78,
  [79] = 79,
  [80] = 78,
  [81] = 71,
  [82] = 76,
  [83] = 75,
  [84] = 70,
  [85] = 72,
  [86] = 74,
  [87] = 87,
  [88] = 88,
  [89] = 89,
  [90] = 90,
  [91] = 91,
  [92] = 92,
  [93] = 93,
  [94] = 94,
  [95] = 95,
  [96] = 96,
  [97] = 97,
  [98] = 95,
  [99] = 99,
  [100] = 100,
  [101] = 101,
  [102] = 102,
  [103] = 103,
  [104] = 104,
  [105] = 105,
  [106] = 106,
  [107] = 107,
  [108] = 108,
  [109] = 109,
  [110] = 110,
  [111] = 111,
  [112] = 112,
  [113] = 113,
  [114] = 114,
  [115] = 115,
  [116] = 116,
  [117] = 117,
  [118] = 118,
  [119] = 119,
  [120] = 120,
  [121] = 121,
  [122] = 122,
  [123] = 105,
  [124] = 102,
  [125] = 125,
  [126] = 126,
  [127] = 113,
  [128] = 128,
  [129] = 129,
  [130] = 109,
  [131] = 104,
  [132] = 99,
  [133] = 114,
  [134] = 134,
  [135] = 135,
  [136] = 116,
  [137] = 128,
  [138] = 138,
  [139] = 106,
  [140] = 140,
  [141] = 140,
  [142] = 142,
  [143] = 143,
};

static bool ts_lex(TSLexer *lexer, TSStateId state) {
  START_LEXER();
  eof = lexer->eof(lexer);
  switch (state) {
    case 0:
      if (eof) ADVANCE(44);
      if (lookahead == '\n') ADVANCE(52);
      if (lookahead == '"') ADVANCE(100);
      if (lookahead == '&') ADVANCE(91);
      if (lookahead == '\'') ADVANCE(101);
      if (lookahead == '-') ADVANCE(53);
      if (lookahead == '/') ADVANCE(48);
      if (lookahead == ':') ADVANCE(51);
      if (lookahead == ';') ADVANCE(92);
      if (lookahead == '<') ADVANCE(47);
      if (lookahead == '=') ADVANCE(93);
      if (lookahead == '>') ADVANCE(49);
      if (lookahead == '?') ADVANCE(55);
      if (lookahead == ']') ADVANCE(56);
      if (lookahead == '_') ADVANCE(60);
      if (lookahead == 'e') ADVANCE(59);
      if (lookahead == 'v') ADVANCE(57);
      if (lookahead == 'x') ADVANCE(58);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(52);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(54);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(60);
      if (lookahead != 0) ADVANCE(52);
      END_STATE();
    case 1:
      if (lookahead == '\n') ADVANCE(99);
      if (lookahead == '-') ADVANCE(96);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(94);
      if (lookahead != 0) ADVANCE(94);
      END_STATE();
    case 2:
      if (lookahead == '\n') ADVANCE(99);
      if (lookahead == '?') ADVANCE(97);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(94);
      if (lookahead != 0) ADVANCE(94);
      END_STATE();
    case 3:
      if (lookahead == '\n') ADVANCE(99);
      if (lookahead == ']') ADVANCE(98);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(94);
      if (lookahead != 0) ADVANCE(94);
      END_STATE();
    case 4:
      if (lookahead == '"') ADVANCE(100);
      if (lookahead == '&') ADVANCE(91);
      if (lookahead == '\'') ADVANCE(101);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(52);
      if (lookahead != 0 &&
          lookahead != '<') ADVANCE(52);
      END_STATE();
    case 5:
      if (lookahead == '"') ADVANCE(100);
      if (lookahead == '\'') ADVANCE(101);
      if (lookahead == '/') ADVANCE(48);
      if (lookahead == ':') ADVANCE(51);
      if (lookahead == ';') ADVANCE(92);
      if (lookahead == '=') ADVANCE(93);
      if (lookahead == '>') ADVANCE(49);
      if (lookahead == '?') ADVANCE(13);
      if (lookahead == ']') ADVANCE(23);
      if (lookahead == 'x') ADVANCE(74);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(99);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(9);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 6:
      if (lookahead == '-') ADVANCE(8);
      END_STATE();
    case 7:
      if (lookahead == '-') ADVANCE(8);
      if (lookahead == '[') ADVANCE(19);
      END_STATE();
    case 8:
      if (lookahead == '-') ADVANCE(84);
      END_STATE();
    case 9:
      if (lookahead == '.') ADVANCE(41);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(9);
      END_STATE();
    case 10:
      if (lookahead == '/') ADVANCE(48);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(99);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 11:
      if (lookahead == ';') ADVANCE(88);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(11);
      END_STATE();
    case 12:
      if (lookahead == ';') ADVANCE(88);
      if (('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'F') ||
          ('a' <= lookahead && lookahead <= 'f')) ADVANCE(12);
      END_STATE();
    case 13:
      if (lookahead == '>') ADVANCE(46);
      END_STATE();
    case 14:
      if (lookahead == '>') ADVANCE(85);
      END_STATE();
    case 15:
      if (lookahead == '>') ADVANCE(90);
      END_STATE();
    case 16:
      if (lookahead == '?') ADVANCE(13);
      if (lookahead == 'e') ADVANCE(32);
      if (lookahead == 'v') ADVANCE(26);
      if (lookahead == 'x') ADVANCE(31);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(99);
      END_STATE();
    case 17:
      if (lookahead == 'A') ADVANCE(21);
      END_STATE();
    case 18:
      if (lookahead == 'A') ADVANCE(22);
      END_STATE();
    case 19:
      if (lookahead == 'C') ADVANCE(20);
      END_STATE();
    case 20:
      if (lookahead == 'D') ADVANCE(17);
      END_STATE();
    case 21:
      if (lookahead == 'T') ADVANCE(18);
      END_STATE();
    case 22:
      if (lookahead == '[') ADVANCE(89);
      END_STATE();
    case 23:
      if (lookahead == ']') ADVANCE(15);
      END_STATE();
    case 24:
      if (lookahead == 'c') ADVANCE(35);
      END_STATE();
    case 25:
      if (lookahead == 'd') ADVANCE(28);
      END_STATE();
    case 26:
      if (lookahead == 'e') ADVANCE(37);
      END_STATE();
    case 27:
      if (lookahead == 'g') ADVANCE(65);
      END_STATE();
    case 28:
      if (lookahead == 'i') ADVANCE(33);
      END_STATE();
    case 29:
      if (lookahead == 'i') ADVANCE(36);
      END_STATE();
    case 30:
      if (lookahead == 'l') ADVANCE(61);
      END_STATE();
    case 31:
      if (lookahead == 'm') ADVANCE(30);
      END_STATE();
    case 32:
      if (lookahead == 'n') ADVANCE(24);
      END_STATE();
    case 33:
      if (lookahead == 'n') ADVANCE(27);
      END_STATE();
    case 34:
      if (lookahead == 'n') ADVANCE(63);
      END_STATE();
    case 35:
      if (lookahead == 'o') ADVANCE(25);
      END_STATE();
    case 36:
      if (lookahead == 'o') ADVANCE(34);
      END_STATE();
    case 37:
      if (lookahead == 'r') ADVANCE(38);
      END_STATE();
    case 38:
      if (lookahead == 's') ADVANCE(29);
      END_STATE();
    case 39:
      if (lookahead == 'x') ADVANCE(42);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(11);
      END_STATE();
    case 40:
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(99);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(103);
      END_STATE();
    case 41:
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(102);
      END_STATE();
    case 42:
      if (('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'F') ||
          ('a' <= lookahead && lookahead <= 'f')) ADVANCE(12);
      END_STATE();
    case 43:
      if (eof) ADVANCE(44);
      if (lookahead == '\n') ADVANCE(99);
      if (lookahead == '<') ADVANCE(95);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(94);
      if (lookahead != 0) ADVANCE(94);
      END_STATE();
    case 44:
      ACCEPT_TOKEN(ts_builtin_sym_end);
      END_STATE();
    case 45:
      ACCEPT_TOKEN(anon_sym_LT_QMARK);
      END_STATE();
    case 46:
      ACCEPT_TOKEN(anon_sym_QMARK_GT);
      END_STATE();
    case 47:
      ACCEPT_TOKEN(anon_sym_LT);
      if (lookahead == '!') ADVANCE(7);
      if (lookahead == '?') ADVANCE(45);
      END_STATE();
    case 48:
      ACCEPT_TOKEN(anon_sym_SLASH);
      END_STATE();
    case 49:
      ACCEPT_TOKEN(anon_sym_GT);
      END_STATE();
    case 50:
      ACCEPT_TOKEN(anon_sym_xmlns);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 51:
      ACCEPT_TOKEN(anon_sym_COLON);
      END_STATE();
    case 52:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      END_STATE();
    case 53:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == '-') ADVANCE(14);
      END_STATE();
    case 54:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == '.') ADVANCE(41);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(9);
      END_STATE();
    case 55:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == '>') ADVANCE(46);
      END_STATE();
    case 56:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == ']') ADVANCE(15);
      END_STATE();
    case 57:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == 'e') ADVANCE(80);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 58:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == 'm') ADVANCE(72);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 59:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == 'n') ADVANCE(67);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 60:
      ACCEPT_TOKEN(aux_sym_attr_value_token1);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 61:
      ACCEPT_TOKEN(anon_sym_xml);
      END_STATE();
    case 62:
      ACCEPT_TOKEN(anon_sym_xml);
      if (lookahead == 'n') ADVANCE(82);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 63:
      ACCEPT_TOKEN(anon_sym_version);
      END_STATE();
    case 64:
      ACCEPT_TOKEN(anon_sym_version);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 65:
      ACCEPT_TOKEN(anon_sym_encoding);
      END_STATE();
    case 66:
      ACCEPT_TOKEN(anon_sym_encoding);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 67:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'c') ADVANCE(78);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 68:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'd') ADVANCE(70);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 69:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'g') ADVANCE(66);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 70:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'i') ADVANCE(75);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 71:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'i') ADVANCE(79);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 72:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'l') ADVANCE(62);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 73:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'l') ADVANCE(77);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 74:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'm') ADVANCE(73);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 75:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'n') ADVANCE(69);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 76:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'n') ADVANCE(64);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 77:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'n') ADVANCE(82);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 78:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'o') ADVANCE(68);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 79:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'o') ADVANCE(76);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 80:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 'r') ADVANCE(81);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 81:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 's') ADVANCE(71);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 82:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == 's') ADVANCE(50);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 83:
      ACCEPT_TOKEN(sym_name);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(83);
      END_STATE();
    case 84:
      ACCEPT_TOKEN(anon_sym_LT_BANG_DASH_DASH);
      END_STATE();
    case 85:
      ACCEPT_TOKEN(anon_sym_DASH_DASH_GT);
      END_STATE();
    case 86:
      ACCEPT_TOKEN(sym_char_data);
      if (lookahead != 0 &&
          (lookahead < '\t' || '\r' < lookahead) &&
          lookahead != ' ' &&
          lookahead != '&' &&
          lookahead != '<') ADVANCE(86);
      END_STATE();
    case 87:
      ACCEPT_TOKEN(sym_char_data);
      if (eof) ADVANCE(44);
      if (lookahead == '&') ADVANCE(91);
      if (lookahead == '<') ADVANCE(47);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') ADVANCE(99);
      if (lookahead != 0) ADVANCE(86);
      END_STATE();
    case 88:
      ACCEPT_TOKEN(sym_char_ref);
      END_STATE();
    case 89:
      ACCEPT_TOKEN(sym_cdata_start);
      END_STATE();
    case 90:
      ACCEPT_TOKEN(sym_cdata_end);
      END_STATE();
    case 91:
      ACCEPT_TOKEN(anon_sym_AMP);
      if (lookahead == '#') ADVANCE(39);
      END_STATE();
    case 92:
      ACCEPT_TOKEN(anon_sym_SEMI);
      END_STATE();
    case 93:
      ACCEPT_TOKEN(sym_eq);
      END_STATE();
    case 94:
      ACCEPT_TOKEN(sym__char);
      END_STATE();
    case 95:
      ACCEPT_TOKEN(sym__char);
      if (lookahead == '!') ADVANCE(6);
      if (lookahead == '?') ADVANCE(45);
      END_STATE();
    case 96:
      ACCEPT_TOKEN(sym__char);
      if (lookahead == '-') ADVANCE(14);
      END_STATE();
    case 97:
      ACCEPT_TOKEN(sym__char);
      if (lookahead == '>') ADVANCE(46);
      END_STATE();
    case 98:
      ACCEPT_TOKEN(sym__char);
      if (lookahead == ']') ADVANCE(15);
      END_STATE();
    case 99:
      ACCEPT_TOKEN(sym__ws);
      END_STATE();
    case 100:
      ACCEPT_TOKEN(anon_sym_DQUOTE);
      END_STATE();
    case 101:
      ACCEPT_TOKEN(anon_sym_SQUOTE);
      END_STATE();
    case 102:
      ACCEPT_TOKEN(sym__dec_num);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(102);
      END_STATE();
    case 103:
      ACCEPT_TOKEN(sym__encoding);
      if (lookahead == '-' ||
          lookahead == '.' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(103);
      END_STATE();
    default:
      return false;
  }
}

static const TSLexMode ts_lex_modes[STATE_COUNT] = {
  [0] = {.lex_state = 0},
  [1] = {.lex_state = 87},
  [2] = {.lex_state = 87},
  [3] = {.lex_state = 87},
  [4] = {.lex_state = 87},
  [5] = {.lex_state = 87},
  [6] = {.lex_state = 87},
  [7] = {.lex_state = 87},
  [8] = {.lex_state = 87},
  [9] = {.lex_state = 87},
  [10] = {.lex_state = 87},
  [11] = {.lex_state = 4},
  [12] = {.lex_state = 4},
  [13] = {.lex_state = 43},
  [14] = {.lex_state = 43},
  [15] = {.lex_state = 43},
  [16] = {.lex_state = 43},
  [17] = {.lex_state = 4},
  [18] = {.lex_state = 43},
  [19] = {.lex_state = 87},
  [20] = {.lex_state = 87},
  [21] = {.lex_state = 87},
  [22] = {.lex_state = 87},
  [23] = {.lex_state = 87},
  [24] = {.lex_state = 87},
  [25] = {.lex_state = 87},
  [26] = {.lex_state = 5},
  [27] = {.lex_state = 87},
  [28] = {.lex_state = 87},
  [29] = {.lex_state = 87},
  [30] = {.lex_state = 87},
  [31] = {.lex_state = 87},
  [32] = {.lex_state = 87},
  [33] = {.lex_state = 87},
  [34] = {.lex_state = 87},
  [35] = {.lex_state = 87},
  [36] = {.lex_state = 87},
  [37] = {.lex_state = 87},
  [38] = {.lex_state = 87},
  [39] = {.lex_state = 87},
  [40] = {.lex_state = 5},
  [41] = {.lex_state = 5},
  [42] = {.lex_state = 87},
  [43] = {.lex_state = 5},
  [44] = {.lex_state = 87},
  [45] = {.lex_state = 5},
  [46] = {.lex_state = 87},
  [47] = {.lex_state = 4},
  [48] = {.lex_state = 43},
  [49] = {.lex_state = 43},
  [50] = {.lex_state = 43},
  [51] = {.lex_state = 43},
  [52] = {.lex_state = 43},
  [53] = {.lex_state = 43},
  [54] = {.lex_state = 5},
  [55] = {.lex_state = 43},
  [56] = {.lex_state = 5},
  [57] = {.lex_state = 43},
  [58] = {.lex_state = 43},
  [59] = {.lex_state = 5},
  [60] = {.lex_state = 43},
  [61] = {.lex_state = 43},
  [62] = {.lex_state = 5},
  [63] = {.lex_state = 5},
  [64] = {.lex_state = 43},
  [65] = {.lex_state = 43},
  [66] = {.lex_state = 43},
  [67] = {.lex_state = 3},
  [68] = {.lex_state = 5},
  [69] = {.lex_state = 5},
  [70] = {.lex_state = 5},
  [71] = {.lex_state = 1},
  [72] = {.lex_state = 2},
  [73] = {.lex_state = 5},
  [74] = {.lex_state = 5},
  [75] = {.lex_state = 1},
  [76] = {.lex_state = 1},
  [77] = {.lex_state = 3},
  [78] = {.lex_state = 2},
  [79] = {.lex_state = 3},
  [80] = {.lex_state = 2},
  [81] = {.lex_state = 2},
  [82] = {.lex_state = 1},
  [83] = {.lex_state = 1},
  [84] = {.lex_state = 5},
  [85] = {.lex_state = 2},
  [86] = {.lex_state = 5},
  [87] = {.lex_state = 5},
  [88] = {.lex_state = 5},
  [89] = {.lex_state = 5},
  [90] = {.lex_state = 5},
  [91] = {.lex_state = 5},
  [92] = {.lex_state = 5},
  [93] = {.lex_state = 5},
  [94] = {.lex_state = 5},
  [95] = {.lex_state = 10},
  [96] = {.lex_state = 16},
  [97] = {.lex_state = 5},
  [98] = {.lex_state = 10},
  [99] = {.lex_state = 10},
  [100] = {.lex_state = 5},
  [101] = {.lex_state = 5},
  [102] = {.lex_state = 5},
  [103] = {.lex_state = 5},
  [104] = {.lex_state = 5},
  [105] = {.lex_state = 5},
  [106] = {.lex_state = 10},
  [107] = {.lex_state = 5},
  [108] = {.lex_state = 16},
  [109] = {.lex_state = 5},
  [110] = {.lex_state = 87},
  [111] = {.lex_state = 5},
  [112] = {.lex_state = 87},
  [113] = {.lex_state = 5},
  [114] = {.lex_state = 5},
  [115] = {.lex_state = 10},
  [116] = {.lex_state = 10},
  [117] = {.lex_state = 5},
  [118] = {.lex_state = 5},
  [119] = {.lex_state = 5},
  [120] = {.lex_state = 40},
  [121] = {.lex_state = 5},
  [122] = {.lex_state = 5},
  [123] = {.lex_state = 5},
  [124] = {.lex_state = 5},
  [125] = {.lex_state = 87},
  [126] = {.lex_state = 10},
  [127] = {.lex_state = 5},
  [128] = {.lex_state = 5},
  [129] = {.lex_state = 5},
  [130] = {.lex_state = 5},
  [131] = {.lex_state = 5},
  [132] = {.lex_state = 10},
  [133] = {.lex_state = 5},
  [134] = {.lex_state = 5},
  [135] = {.lex_state = 5},
  [136] = {.lex_state = 10},
  [137] = {.lex_state = 5},
  [138] = {.lex_state = 16},
  [139] = {.lex_state = 10},
  [140] = {.lex_state = 10},
  [141] = {.lex_state = 10},
  [142] = {.lex_state = 87},
  [143] = {.lex_state = 87},
};

static const uint16_t ts_parse_table[LARGE_STATE_COUNT][SYMBOL_COUNT] = {
  [0] = {
    [ts_builtin_sym_end] = ACTIONS(1),
    [anon_sym_LT_QMARK] = ACTIONS(1),
    [anon_sym_QMARK_GT] = ACTIONS(1),
    [anon_sym_LT] = ACTIONS(1),
    [anon_sym_SLASH] = ACTIONS(1),
    [anon_sym_GT] = ACTIONS(1),
    [anon_sym_xmlns] = ACTIONS(1),
    [anon_sym_COLON] = ACTIONS(1),
    [aux_sym_attr_value_token1] = ACTIONS(1),
    [anon_sym_xml] = ACTIONS(1),
    [anon_sym_version] = ACTIONS(1),
    [anon_sym_encoding] = ACTIONS(1),
    [sym_name] = ACTIONS(1),
    [anon_sym_LT_BANG_DASH_DASH] = ACTIONS(1),
    [anon_sym_DASH_DASH_GT] = ACTIONS(1),
    [sym_char_ref] = ACTIONS(1),
    [sym_cdata_start] = ACTIONS(1),
    [sym_cdata_end] = ACTIONS(1),
    [anon_sym_AMP] = ACTIONS(1),
    [anon_sym_SEMI] = ACTIONS(1),
    [sym_eq] = ACTIONS(1),
    [sym__char] = ACTIONS(1),
    [sym__ws] = ACTIONS(3),
    [anon_sym_DQUOTE] = ACTIONS(1),
    [anon_sym_SQUOTE] = ACTIONS(1),
    [sym__dec_num] = ACTIONS(1),
    [sym__encoding] = ACTIONS(1),
  },
  [1] = {
    [sym_xml_file] = STATE(110),
    [sym_element] = STATE(18),
    [sym_end_tag_element] = STATE(66),
    [sym_empty_element] = STATE(66),
    [sym_tag_start] = STATE(2),
    [sym_xml_decl] = STATE(46),
    [anon_sym_LT_QMARK] = ACTIONS(5),
    [anon_sym_LT] = ACTIONS(7),
    [sym__ws] = ACTIONS(9),
  },
};

static const uint16_t ts_small_parse_table[] = {
  [0] = 13,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(13), 1,
      anon_sym_LT,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(17), 1,
      sym_char_data,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    STATE(3), 1,
      sym_tag_start,
    STATE(6), 1,
      aux_sym__content,
    STATE(50), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [46] = 13,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    ACTIONS(25), 1,
      anon_sym_LT,
    ACTIONS(27), 1,
      sym_char_data,
    STATE(3), 1,
      sym_tag_start,
    STATE(9), 1,
      aux_sym__content,
    STATE(28), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [92] = 12,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    ACTIONS(25), 1,
      anon_sym_LT,
    STATE(3), 1,
      sym_tag_start,
    STATE(10), 1,
      aux_sym__content,
    STATE(31), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [135] = 12,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(13), 1,
      anon_sym_LT,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    STATE(3), 1,
      sym_tag_start,
    STATE(7), 1,
      aux_sym__content,
    STATE(65), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [178] = 12,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(13), 1,
      anon_sym_LT,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    STATE(3), 1,
      sym_tag_start,
    STATE(10), 1,
      aux_sym__content,
    STATE(65), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [221] = 12,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(13), 1,
      anon_sym_LT,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    STATE(3), 1,
      sym_tag_start,
    STATE(10), 1,
      aux_sym__content,
    STATE(53), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [264] = 12,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    ACTIONS(25), 1,
      anon_sym_LT,
    STATE(3), 1,
      sym_tag_start,
    STATE(4), 1,
      aux_sym__content,
    STATE(29), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [307] = 12,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(11), 1,
      anon_sym_LT_QMARK,
    ACTIONS(15), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(19), 1,
      sym_char_ref,
    ACTIONS(21), 1,
      sym_cdata_start,
    ACTIONS(23), 1,
      anon_sym_AMP,
    ACTIONS(25), 1,
      anon_sym_LT,
    STATE(3), 1,
      sym_tag_start,
    STATE(10), 1,
      aux_sym__content,
    STATE(29), 1,
      sym_tag_end,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [350] = 11,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(29), 1,
      anon_sym_LT_QMARK,
    ACTIONS(32), 1,
      anon_sym_LT,
    ACTIONS(35), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(38), 1,
      sym_char_ref,
    ACTIONS(41), 1,
      sym_cdata_start,
    ACTIONS(44), 1,
      anon_sym_AMP,
    STATE(3), 1,
      sym_tag_start,
    STATE(10), 1,
      aux_sym__content,
    STATE(27), 2,
      sym_end_tag_element,
      sym_empty_element,
    STATE(34), 6,
      sym_pi,
      sym_element,
      sym_comment,
      sym_cdata_sect,
      sym__ref,
      sym_entity_ref,
  [390] = 7,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(47), 1,
      aux_sym_attr_value_token1,
    ACTIONS(49), 1,
      sym_char_ref,
    ACTIONS(51), 1,
      anon_sym_AMP,
    STATE(91), 1,
      sym__quote,
    ACTIONS(53), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
    STATE(12), 3,
      sym__ref,
      sym_entity_ref,
      aux_sym_attr_value_repeat1,
  [415] = 7,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(51), 1,
      anon_sym_AMP,
    ACTIONS(55), 1,
      aux_sym_attr_value_token1,
    ACTIONS(57), 1,
      sym_char_ref,
    STATE(94), 1,
      sym__quote,
    ACTIONS(59), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
    STATE(17), 3,
      sym__ref,
      sym_entity_ref,
      aux_sym_attr_value_repeat1,
  [440] = 6,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(61), 1,
      ts_builtin_sym_end,
    ACTIONS(63), 1,
      anon_sym_LT_QMARK,
    ACTIONS(65), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(67), 1,
      sym__char,
    STATE(16), 4,
      sym_pi,
      sym_comment,
      sym__misc,
      aux_sym_xml_file_repeat1,
  [462] = 6,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(63), 1,
      anon_sym_LT_QMARK,
    ACTIONS(65), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(69), 1,
      ts_builtin_sym_end,
    ACTIONS(71), 1,
      sym__char,
    STATE(13), 4,
      sym_pi,
      sym_comment,
      sym__misc,
      aux_sym_xml_file_repeat1,
  [484] = 6,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(63), 1,
      anon_sym_LT_QMARK,
    ACTIONS(65), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(67), 1,
      sym__char,
    ACTIONS(69), 1,
      ts_builtin_sym_end,
    STATE(16), 4,
      sym_pi,
      sym_comment,
      sym__misc,
      aux_sym_xml_file_repeat1,
  [506] = 6,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(73), 1,
      ts_builtin_sym_end,
    ACTIONS(75), 1,
      anon_sym_LT_QMARK,
    ACTIONS(78), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(81), 1,
      sym__char,
    STATE(16), 4,
      sym_pi,
      sym_comment,
      sym__misc,
      aux_sym_xml_file_repeat1,
  [528] = 6,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(84), 1,
      aux_sym_attr_value_token1,
    ACTIONS(87), 1,
      sym_char_ref,
    ACTIONS(90), 1,
      anon_sym_AMP,
    ACTIONS(93), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
    STATE(17), 3,
      sym__ref,
      sym_entity_ref,
      aux_sym_attr_value_repeat1,
  [550] = 6,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(63), 1,
      anon_sym_LT_QMARK,
    ACTIONS(65), 1,
      anon_sym_LT_BANG_DASH_DASH,
    ACTIONS(95), 1,
      ts_builtin_sym_end,
    ACTIONS(97), 1,
      sym__char,
    STATE(15), 4,
      sym_pi,
      sym_comment,
      sym__misc,
      aux_sym_xml_file_repeat1,
  [572] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(101), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(99), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [587] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(105), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(103), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [602] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(109), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(107), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [617] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(113), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(111), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [632] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(117), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(115), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [647] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(121), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(119), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [662] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(125), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(123), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [677] = 7,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(127), 1,
      anon_sym_SLASH,
    ACTIONS(129), 1,
      anon_sym_GT,
    ACTIONS(131), 1,
      anon_sym_xmlns,
    ACTIONS(133), 1,
      sym_name,
    STATE(89), 1,
      sym_attribute,
    STATE(90), 2,
      sym_ns_decl,
      sym_xml_attr,
  [700] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(137), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(135), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [715] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(141), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(139), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [730] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(145), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(143), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [745] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(149), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(147), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [760] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(153), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(151), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [775] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(157), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(155), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [790] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(161), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(159), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [805] = 4,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(167), 1,
      sym_char_data,
    ACTIONS(165), 2,
      anon_sym_LT,
      anon_sym_AMP,
    ACTIONS(163), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [822] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(171), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(169), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [837] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(175), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(173), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [852] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(179), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(177), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [867] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(183), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(181), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [882] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(187), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(185), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [897] = 7,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(131), 1,
      anon_sym_xmlns,
    ACTIONS(133), 1,
      sym_name,
    ACTIONS(189), 1,
      anon_sym_SLASH,
    ACTIONS(191), 1,
      anon_sym_GT,
    STATE(89), 1,
      sym_attribute,
    STATE(90), 2,
      sym_ns_decl,
      sym_xml_attr,
  [920] = 7,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(129), 1,
      anon_sym_GT,
    ACTIONS(131), 1,
      anon_sym_xmlns,
    ACTIONS(133), 1,
      sym_name,
    ACTIONS(193), 1,
      anon_sym_SLASH,
    STATE(89), 1,
      sym_attribute,
    STATE(90), 2,
      sym_ns_decl,
      sym_xml_attr,
  [943] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(197), 3,
      anon_sym_LT,
      sym_char_data,
      anon_sym_AMP,
    ACTIONS(195), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [958] = 7,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(131), 1,
      anon_sym_xmlns,
    ACTIONS(133), 1,
      sym_name,
    ACTIONS(191), 1,
      anon_sym_GT,
    ACTIONS(199), 1,
      anon_sym_SLASH,
    STATE(89), 1,
      sym_attribute,
    STATE(90), 2,
      sym_ns_decl,
      sym_xml_attr,
  [981] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(203), 2,
      anon_sym_LT,
      anon_sym_AMP,
    ACTIONS(201), 4,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
      sym_char_ref,
      sym_cdata_start,
  [995] = 5,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(131), 1,
      anon_sym_xmlns,
    ACTIONS(133), 1,
      sym_name,
    STATE(89), 1,
      sym_attribute,
    STATE(90), 2,
      sym_ns_decl,
      sym_xml_attr,
  [1012] = 5,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(205), 1,
      anon_sym_LT,
    STATE(2), 1,
      sym_tag_start,
    STATE(14), 1,
      sym_element,
    STATE(66), 2,
      sym_end_tag_element,
      sym_empty_element,
  [1029] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(197), 2,
      aux_sym_attr_value_token1,
      anon_sym_AMP,
    ACTIONS(195), 3,
      sym_char_ref,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1042] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(175), 1,
      sym__char,
    ACTIONS(173), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1054] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(125), 1,
      sym__char,
    ACTIONS(123), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1066] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(141), 1,
      sym__char,
    ACTIONS(139), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1078] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(105), 1,
      sym__char,
    ACTIONS(103), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1090] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(149), 1,
      sym__char,
    ACTIONS(147), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1102] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(153), 1,
      sym__char,
    ACTIONS(151), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1114] = 4,
    ACTIONS(9), 1,
      sym__ws,
    STATE(121), 1,
      sym_xml_version_value,
    STATE(129), 1,
      sym__quote,
    ACTIONS(207), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1128] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(161), 1,
      sym__char,
    ACTIONS(159), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1140] = 4,
    ACTIONS(9), 1,
      sym__ws,
    STATE(11), 1,
      sym__quote,
    STATE(87), 1,
      sym_attr_value,
    ACTIONS(209), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1154] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(101), 1,
      sym__char,
    ACTIONS(99), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1166] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(171), 1,
      sym__char,
    ACTIONS(169), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1178] = 4,
    ACTIONS(9), 1,
      sym__ws,
    STATE(118), 1,
      sym_xml_encoding_value,
    STATE(120), 1,
      sym__quote,
    ACTIONS(211), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1192] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(179), 1,
      sym__char,
    ACTIONS(177), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1204] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(183), 1,
      sym__char,
    ACTIONS(181), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1216] = 4,
    ACTIONS(9), 1,
      sym__ws,
    STATE(11), 1,
      sym__quote,
    STATE(92), 1,
      sym_attr_value,
    ACTIONS(209), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1230] = 4,
    ACTIONS(9), 1,
      sym__ws,
    STATE(11), 1,
      sym__quote,
    STATE(93), 1,
      sym_attr_value,
    ACTIONS(209), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1244] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(113), 1,
      sym__char,
    ACTIONS(111), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1256] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(145), 1,
      sym__char,
    ACTIONS(143), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1268] = 3,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(137), 1,
      sym__char,
    ACTIONS(135), 3,
      ts_builtin_sym_end,
      anon_sym_LT_QMARK,
      anon_sym_LT_BANG_DASH_DASH,
  [1280] = 5,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(213), 1,
      sym_cdata_end,
    ACTIONS(215), 1,
      sym__char,
    STATE(79), 1,
      aux_sym_pi_repeat1,
    STATE(111), 1,
      sym_cdata,
  [1296] = 3,
    ACTIONS(9), 1,
      sym__ws,
    STATE(101), 1,
      sym__quote,
    ACTIONS(217), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1307] = 3,
    ACTIONS(221), 1,
      sym__ws,
    STATE(69), 1,
      aux_sym_empty_element_repeat1,
    ACTIONS(219), 2,
      anon_sym_SLASH,
      anon_sym_GT,
  [1318] = 4,
    ACTIONS(224), 1,
      anon_sym_SLASH,
    ACTIONS(226), 1,
      anon_sym_GT,
    ACTIONS(228), 1,
      sym__ws,
    STATE(74), 1,
      aux_sym_empty_element_repeat1,
  [1331] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(230), 1,
      anon_sym_DASH_DASH_GT,
    ACTIONS(232), 1,
      sym__char,
    STATE(71), 1,
      aux_sym_pi_repeat1,
  [1344] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(235), 1,
      anon_sym_QMARK_GT,
    ACTIONS(237), 1,
      sym__char,
    STATE(81), 1,
      aux_sym_pi_repeat1,
  [1357] = 3,
    ACTIONS(9), 1,
      sym__ws,
    STATE(134), 1,
      sym__quote,
    ACTIONS(239), 2,
      anon_sym_DQUOTE,
      anon_sym_SQUOTE,
  [1368] = 4,
    ACTIONS(127), 1,
      anon_sym_SLASH,
    ACTIONS(129), 1,
      anon_sym_GT,
    ACTIONS(241), 1,
      sym__ws,
    STATE(69), 1,
      aux_sym_empty_element_repeat1,
  [1381] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(243), 1,
      anon_sym_DASH_DASH_GT,
    ACTIONS(245), 1,
      sym__char,
    STATE(71), 1,
      aux_sym_pi_repeat1,
  [1394] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(247), 1,
      anon_sym_DASH_DASH_GT,
    ACTIONS(249), 1,
      sym__char,
    STATE(75), 1,
      aux_sym_pi_repeat1,
  [1407] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(230), 1,
      sym_cdata_end,
    ACTIONS(251), 1,
      sym__char,
    STATE(77), 1,
      aux_sym_pi_repeat1,
  [1420] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(254), 1,
      anon_sym_QMARK_GT,
    ACTIONS(256), 1,
      sym__char,
    STATE(85), 1,
      aux_sym_pi_repeat1,
  [1433] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(258), 1,
      sym_cdata_end,
    ACTIONS(260), 1,
      sym__char,
    STATE(77), 1,
      aux_sym_pi_repeat1,
  [1446] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(262), 1,
      anon_sym_QMARK_GT,
    ACTIONS(264), 1,
      sym__char,
    STATE(72), 1,
      aux_sym_pi_repeat1,
  [1459] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(230), 1,
      anon_sym_QMARK_GT,
    ACTIONS(266), 1,
      sym__char,
    STATE(81), 1,
      aux_sym_pi_repeat1,
  [1472] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(269), 1,
      anon_sym_DASH_DASH_GT,
    ACTIONS(271), 1,
      sym__char,
    STATE(83), 1,
      aux_sym_pi_repeat1,
  [1485] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(245), 1,
      sym__char,
    ACTIONS(273), 1,
      anon_sym_DASH_DASH_GT,
    STATE(71), 1,
      aux_sym_pi_repeat1,
  [1498] = 4,
    ACTIONS(226), 1,
      anon_sym_GT,
    ACTIONS(275), 1,
      anon_sym_SLASH,
    ACTIONS(277), 1,
      sym__ws,
    STATE(86), 1,
      aux_sym_empty_element_repeat1,
  [1511] = 4,
    ACTIONS(3), 1,
      sym__ws,
    ACTIONS(237), 1,
      sym__char,
    ACTIONS(279), 1,
      anon_sym_QMARK_GT,
    STATE(81), 1,
      aux_sym_pi_repeat1,
  [1524] = 4,
    ACTIONS(129), 1,
      anon_sym_GT,
    ACTIONS(193), 1,
      anon_sym_SLASH,
    ACTIONS(281), 1,
      sym__ws,
    STATE(69), 1,
      aux_sym_empty_element_repeat1,
  [1537] = 1,
    ACTIONS(283), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1543] = 3,
    ACTIONS(285), 1,
      anon_sym_QMARK_GT,
    ACTIONS(287), 1,
      sym__ws,
    STATE(122), 1,
      sym_xml_encoding,
  [1553] = 1,
    ACTIONS(219), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1559] = 1,
    ACTIONS(289), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1565] = 1,
    ACTIONS(291), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1571] = 1,
    ACTIONS(293), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1577] = 1,
    ACTIONS(295), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1583] = 1,
    ACTIONS(297), 3,
      anon_sym_SLASH,
      anon_sym_GT,
      sym__ws,
  [1589] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(299), 1,
      anon_sym_SLASH,
    ACTIONS(301), 1,
      sym_name,
  [1599] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(303), 1,
      anon_sym_QMARK_GT,
    ACTIONS(305), 1,
      anon_sym_encoding,
  [1609] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(307), 1,
      anon_sym_COLON,
    ACTIONS(309), 1,
      sym_eq,
  [1619] = 3,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(301), 1,
      sym_name,
    ACTIONS(311), 1,
      anon_sym_SLASH,
  [1629] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(313), 1,
      sym_name,
  [1636] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(315), 1,
      sym_eq,
  [1643] = 1,
    ACTIONS(317), 2,
      anon_sym_QMARK_GT,
      sym__ws,
  [1648] = 2,
    ACTIONS(319), 1,
      anon_sym_QMARK_GT,
    ACTIONS(321), 1,
      sym__ws,
  [1655] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(323), 1,
      sym_eq,
  [1662] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(325), 1,
      anon_sym_GT,
  [1669] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(327), 1,
      anon_sym_GT,
  [1676] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(329), 1,
      sym_name,
  [1683] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(331), 1,
      anon_sym_QMARK_GT,
  [1690] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(333), 1,
      anon_sym_xml,
  [1697] = 2,
    ACTIONS(335), 1,
      anon_sym_GT,
    ACTIONS(337), 1,
      sym__ws,
  [1704] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(339), 1,
      ts_builtin_sym_end,
  [1711] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(341), 1,
      sym_cdata_end,
  [1718] = 2,
    ACTIONS(343), 1,
      sym__ws,
    STATE(88), 1,
      sym_xml_version,
  [1725] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(345), 1,
      anon_sym_SEMI,
  [1732] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(347), 1,
      anon_sym_GT,
  [1739] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(349), 1,
      sym_name,
  [1746] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(351), 1,
      sym_name,
  [1753] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(353), 1,
      sym_eq,
  [1760] = 1,
    ACTIONS(355), 2,
      anon_sym_QMARK_GT,
      sym__ws,
  [1765] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(357), 1,
      sym_eq,
  [1772] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(359), 1,
      sym__encoding,
  [1779] = 1,
    ACTIONS(361), 2,
      anon_sym_QMARK_GT,
      sym__ws,
  [1784] = 2,
    ACTIONS(303), 1,
      anon_sym_QMARK_GT,
    ACTIONS(363), 1,
      sym__ws,
  [1791] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(365), 1,
      anon_sym_GT,
  [1798] = 2,
    ACTIONS(367), 1,
      anon_sym_QMARK_GT,
    ACTIONS(369), 1,
      sym__ws,
  [1805] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(371), 1,
      anon_sym_LT,
  [1812] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(373), 1,
      sym_name,
  [1819] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(375), 1,
      anon_sym_SEMI,
  [1826] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(377), 1,
      anon_sym_GT,
  [1833] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(379), 1,
      sym__dec_num,
  [1840] = 2,
    ACTIONS(381), 1,
      anon_sym_GT,
    ACTIONS(383), 1,
      sym__ws,
  [1847] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(385), 1,
      anon_sym_GT,
  [1854] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(387), 1,
      sym_name,
  [1861] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(389), 1,
      anon_sym_GT,
  [1868] = 1,
    ACTIONS(391), 2,
      anon_sym_QMARK_GT,
      sym__ws,
  [1873] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(393), 1,
      anon_sym_COLON,
  [1880] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(395), 1,
      sym_name,
  [1887] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(397), 1,
      anon_sym_GT,
  [1894] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(399), 1,
      anon_sym_version,
  [1901] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(401), 1,
      sym_name,
  [1908] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(403), 1,
      sym_name,
  [1915] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(301), 1,
      sym_name,
  [1922] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(405), 1,
      anon_sym_LT,
  [1929] = 2,
    ACTIONS(9), 1,
      sym__ws,
    ACTIONS(407), 1,
      anon_sym_LT,
};

static const uint32_t ts_small_parse_table_map[] = {
  [SMALL_STATE(2)] = 0,
  [SMALL_STATE(3)] = 46,
  [SMALL_STATE(4)] = 92,
  [SMALL_STATE(5)] = 135,
  [SMALL_STATE(6)] = 178,
  [SMALL_STATE(7)] = 221,
  [SMALL_STATE(8)] = 264,
  [SMALL_STATE(9)] = 307,
  [SMALL_STATE(10)] = 350,
  [SMALL_STATE(11)] = 390,
  [SMALL_STATE(12)] = 415,
  [SMALL_STATE(13)] = 440,
  [SMALL_STATE(14)] = 462,
  [SMALL_STATE(15)] = 484,
  [SMALL_STATE(16)] = 506,
  [SMALL_STATE(17)] = 528,
  [SMALL_STATE(18)] = 550,
  [SMALL_STATE(19)] = 572,
  [SMALL_STATE(20)] = 587,
  [SMALL_STATE(21)] = 602,
  [SMALL_STATE(22)] = 617,
  [SMALL_STATE(23)] = 632,
  [SMALL_STATE(24)] = 647,
  [SMALL_STATE(25)] = 662,
  [SMALL_STATE(26)] = 677,
  [SMALL_STATE(27)] = 700,
  [SMALL_STATE(28)] = 715,
  [SMALL_STATE(29)] = 730,
  [SMALL_STATE(30)] = 745,
  [SMALL_STATE(31)] = 760,
  [SMALL_STATE(32)] = 775,
  [SMALL_STATE(33)] = 790,
  [SMALL_STATE(34)] = 805,
  [SMALL_STATE(35)] = 822,
  [SMALL_STATE(36)] = 837,
  [SMALL_STATE(37)] = 852,
  [SMALL_STATE(38)] = 867,
  [SMALL_STATE(39)] = 882,
  [SMALL_STATE(40)] = 897,
  [SMALL_STATE(41)] = 920,
  [SMALL_STATE(42)] = 943,
  [SMALL_STATE(43)] = 958,
  [SMALL_STATE(44)] = 981,
  [SMALL_STATE(45)] = 995,
  [SMALL_STATE(46)] = 1012,
  [SMALL_STATE(47)] = 1029,
  [SMALL_STATE(48)] = 1042,
  [SMALL_STATE(49)] = 1054,
  [SMALL_STATE(50)] = 1066,
  [SMALL_STATE(51)] = 1078,
  [SMALL_STATE(52)] = 1090,
  [SMALL_STATE(53)] = 1102,
  [SMALL_STATE(54)] = 1114,
  [SMALL_STATE(55)] = 1128,
  [SMALL_STATE(56)] = 1140,
  [SMALL_STATE(57)] = 1154,
  [SMALL_STATE(58)] = 1166,
  [SMALL_STATE(59)] = 1178,
  [SMALL_STATE(60)] = 1192,
  [SMALL_STATE(61)] = 1204,
  [SMALL_STATE(62)] = 1216,
  [SMALL_STATE(63)] = 1230,
  [SMALL_STATE(64)] = 1244,
  [SMALL_STATE(65)] = 1256,
  [SMALL_STATE(66)] = 1268,
  [SMALL_STATE(67)] = 1280,
  [SMALL_STATE(68)] = 1296,
  [SMALL_STATE(69)] = 1307,
  [SMALL_STATE(70)] = 1318,
  [SMALL_STATE(71)] = 1331,
  [SMALL_STATE(72)] = 1344,
  [SMALL_STATE(73)] = 1357,
  [SMALL_STATE(74)] = 1368,
  [SMALL_STATE(75)] = 1381,
  [SMALL_STATE(76)] = 1394,
  [SMALL_STATE(77)] = 1407,
  [SMALL_STATE(78)] = 1420,
  [SMALL_STATE(79)] = 1433,
  [SMALL_STATE(80)] = 1446,
  [SMALL_STATE(81)] = 1459,
  [SMALL_STATE(82)] = 1472,
  [SMALL_STATE(83)] = 1485,
  [SMALL_STATE(84)] = 1498,
  [SMALL_STATE(85)] = 1511,
  [SMALL_STATE(86)] = 1524,
  [SMALL_STATE(87)] = 1537,
  [SMALL_STATE(88)] = 1543,
  [SMALL_STATE(89)] = 1553,
  [SMALL_STATE(90)] = 1559,
  [SMALL_STATE(91)] = 1565,
  [SMALL_STATE(92)] = 1571,
  [SMALL_STATE(93)] = 1577,
  [SMALL_STATE(94)] = 1583,
  [SMALL_STATE(95)] = 1589,
  [SMALL_STATE(96)] = 1599,
  [SMALL_STATE(97)] = 1609,
  [SMALL_STATE(98)] = 1619,
  [SMALL_STATE(99)] = 1629,
  [SMALL_STATE(100)] = 1636,
  [SMALL_STATE(101)] = 1643,
  [SMALL_STATE(102)] = 1648,
  [SMALL_STATE(103)] = 1655,
  [SMALL_STATE(104)] = 1662,
  [SMALL_STATE(105)] = 1669,
  [SMALL_STATE(106)] = 1676,
  [SMALL_STATE(107)] = 1683,
  [SMALL_STATE(108)] = 1690,
  [SMALL_STATE(109)] = 1697,
  [SMALL_STATE(110)] = 1704,
  [SMALL_STATE(111)] = 1711,
  [SMALL_STATE(112)] = 1718,
  [SMALL_STATE(113)] = 1725,
  [SMALL_STATE(114)] = 1732,
  [SMALL_STATE(115)] = 1739,
  [SMALL_STATE(116)] = 1746,
  [SMALL_STATE(117)] = 1753,
  [SMALL_STATE(118)] = 1760,
  [SMALL_STATE(119)] = 1765,
  [SMALL_STATE(120)] = 1772,
  [SMALL_STATE(121)] = 1779,
  [SMALL_STATE(122)] = 1784,
  [SMALL_STATE(123)] = 1791,
  [SMALL_STATE(124)] = 1798,
  [SMALL_STATE(125)] = 1805,
  [SMALL_STATE(126)] = 1812,
  [SMALL_STATE(127)] = 1819,
  [SMALL_STATE(128)] = 1826,
  [SMALL_STATE(129)] = 1833,
  [SMALL_STATE(130)] = 1840,
  [SMALL_STATE(131)] = 1847,
  [SMALL_STATE(132)] = 1854,
  [SMALL_STATE(133)] = 1861,
  [SMALL_STATE(134)] = 1868,
  [SMALL_STATE(135)] = 1873,
  [SMALL_STATE(136)] = 1880,
  [SMALL_STATE(137)] = 1887,
  [SMALL_STATE(138)] = 1894,
  [SMALL_STATE(139)] = 1901,
  [SMALL_STATE(140)] = 1908,
  [SMALL_STATE(141)] = 1915,
  [SMALL_STATE(142)] = 1922,
  [SMALL_STATE(143)] = 1929,
};

static const TSParseActionEntry ts_parse_actions[] = {
  [0] = {.entry = {.count = 0, .reusable = false}},
  [1] = {.entry = {.count = 1, .reusable = false}}, RECOVER(),
  [3] = {.entry = {.count = 1, .reusable = false}}, SHIFT_EXTRA(),
  [5] = {.entry = {.count = 1, .reusable = true}}, SHIFT(108),
  [7] = {.entry = {.count = 1, .reusable = false}}, SHIFT(140),
  [9] = {.entry = {.count = 1, .reusable = true}}, SHIFT_EXTRA(),
  [11] = {.entry = {.count = 1, .reusable = true}}, SHIFT(99),
  [13] = {.entry = {.count = 1, .reusable = false}}, SHIFT(95),
  [15] = {.entry = {.count = 1, .reusable = true}}, SHIFT(82),
  [17] = {.entry = {.count = 1, .reusable = false}}, SHIFT(5),
  [19] = {.entry = {.count = 1, .reusable = true}}, SHIFT(34),
  [21] = {.entry = {.count = 1, .reusable = true}}, SHIFT(67),
  [23] = {.entry = {.count = 1, .reusable = false}}, SHIFT(116),
  [25] = {.entry = {.count = 1, .reusable = false}}, SHIFT(98),
  [27] = {.entry = {.count = 1, .reusable = false}}, SHIFT(8),
  [29] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym__content, 2), SHIFT_REPEAT(99),
  [32] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym__content, 2), SHIFT_REPEAT(141),
  [35] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym__content, 2), SHIFT_REPEAT(82),
  [38] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym__content, 2), SHIFT_REPEAT(34),
  [41] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym__content, 2), SHIFT_REPEAT(67),
  [44] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym__content, 2), SHIFT_REPEAT(116),
  [47] = {.entry = {.count = 1, .reusable = false}}, SHIFT(12),
  [49] = {.entry = {.count = 1, .reusable = true}}, SHIFT(12),
  [51] = {.entry = {.count = 1, .reusable = false}}, SHIFT(136),
  [53] = {.entry = {.count = 1, .reusable = true}}, SHIFT(91),
  [55] = {.entry = {.count = 1, .reusable = false}}, SHIFT(17),
  [57] = {.entry = {.count = 1, .reusable = true}}, SHIFT(17),
  [59] = {.entry = {.count = 1, .reusable = true}}, SHIFT(94),
  [61] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_file, 3),
  [63] = {.entry = {.count = 1, .reusable = true}}, SHIFT(132),
  [65] = {.entry = {.count = 1, .reusable = true}}, SHIFT(76),
  [67] = {.entry = {.count = 1, .reusable = false}}, SHIFT(16),
  [69] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_file, 2),
  [71] = {.entry = {.count = 1, .reusable = false}}, SHIFT(13),
  [73] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_xml_file_repeat1, 2),
  [75] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_xml_file_repeat1, 2), SHIFT_REPEAT(132),
  [78] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_xml_file_repeat1, 2), SHIFT_REPEAT(76),
  [81] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_xml_file_repeat1, 2), SHIFT_REPEAT(16),
  [84] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_attr_value_repeat1, 2), SHIFT_REPEAT(17),
  [87] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_attr_value_repeat1, 2), SHIFT_REPEAT(17),
  [90] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_attr_value_repeat1, 2), SHIFT_REPEAT(136),
  [93] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_attr_value_repeat1, 2),
  [95] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_file, 1),
  [97] = {.entry = {.count = 1, .reusable = false}}, SHIFT(15),
  [99] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_pi, 4),
  [101] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_pi, 4),
  [103] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_pi, 3),
  [105] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_pi, 3),
  [107] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_tag_start, 5, .production_id = 1),
  [109] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_tag_start, 5, .production_id = 1),
  [111] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_comment, 2),
  [113] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_comment, 2),
  [115] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_tag_start, 3, .production_id = 1),
  [117] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_tag_start, 3, .production_id = 1),
  [119] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_cdata_sect, 2),
  [121] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_cdata_sect, 2),
  [123] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_empty_element, 4, .production_id = 1),
  [125] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_empty_element, 4, .production_id = 1),
  [127] = {.entry = {.count = 1, .reusable = true}}, SHIFT(137),
  [129] = {.entry = {.count = 1, .reusable = true}}, SHIFT(39),
  [131] = {.entry = {.count = 1, .reusable = false}}, SHIFT(135),
  [133] = {.entry = {.count = 1, .reusable = false}}, SHIFT(97),
  [135] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_element, 1),
  [137] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_element, 1),
  [139] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_end_tag_element, 2),
  [141] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_end_tag_element, 2),
  [143] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_end_tag_element, 3),
  [145] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_end_tag_element, 3),
  [147] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_comment, 3),
  [149] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_comment, 3),
  [151] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_end_tag_element, 4),
  [153] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_end_tag_element, 4),
  [155] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_cdata_sect, 3),
  [157] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_cdata_sect, 3),
  [159] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_empty_element, 5, .production_id = 1),
  [161] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_empty_element, 5, .production_id = 1),
  [163] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym__content, 1),
  [165] = {.entry = {.count = 1, .reusable = false}}, REDUCE(aux_sym__content, 1),
  [167] = {.entry = {.count = 1, .reusable = false}}, SHIFT(44),
  [169] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_tag_end, 4, .production_id = 3),
  [171] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_tag_end, 4, .production_id = 3),
  [173] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_empty_element, 6, .production_id = 1),
  [175] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_empty_element, 6, .production_id = 1),
  [177] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_pi, 5),
  [179] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_pi, 5),
  [181] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_tag_end, 5, .production_id = 3),
  [183] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_tag_end, 5, .production_id = 3),
  [185] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_tag_start, 4, .production_id = 1),
  [187] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_tag_start, 4, .production_id = 1),
  [189] = {.entry = {.count = 1, .reusable = true}}, SHIFT(104),
  [191] = {.entry = {.count = 1, .reusable = true}}, SHIFT(21),
  [193] = {.entry = {.count = 1, .reusable = true}}, SHIFT(128),
  [195] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_entity_ref, 3),
  [197] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_entity_ref, 3),
  [199] = {.entry = {.count = 1, .reusable = true}}, SHIFT(131),
  [201] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym__content, 2),
  [203] = {.entry = {.count = 1, .reusable = false}}, REDUCE(aux_sym__content, 2),
  [205] = {.entry = {.count = 1, .reusable = true}}, SHIFT(140),
  [207] = {.entry = {.count = 1, .reusable = true}}, SHIFT(129),
  [209] = {.entry = {.count = 1, .reusable = true}}, SHIFT(11),
  [211] = {.entry = {.count = 1, .reusable = true}}, SHIFT(120),
  [213] = {.entry = {.count = 1, .reusable = true}}, SHIFT(24),
  [215] = {.entry = {.count = 1, .reusable = false}}, SHIFT(79),
  [217] = {.entry = {.count = 1, .reusable = true}}, SHIFT(101),
  [219] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_empty_element_repeat1, 2),
  [221] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_empty_element_repeat1, 2), SHIFT_REPEAT(45),
  [224] = {.entry = {.count = 1, .reusable = true}}, SHIFT(105),
  [226] = {.entry = {.count = 1, .reusable = true}}, SHIFT(23),
  [228] = {.entry = {.count = 1, .reusable = true}}, SHIFT(26),
  [230] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_pi_repeat1, 2),
  [232] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pi_repeat1, 2), SHIFT_REPEAT(71),
  [235] = {.entry = {.count = 1, .reusable = true}}, SHIFT(60),
  [237] = {.entry = {.count = 1, .reusable = false}}, SHIFT(81),
  [239] = {.entry = {.count = 1, .reusable = true}}, SHIFT(134),
  [241] = {.entry = {.count = 1, .reusable = true}}, SHIFT(40),
  [243] = {.entry = {.count = 1, .reusable = true}}, SHIFT(52),
  [245] = {.entry = {.count = 1, .reusable = false}}, SHIFT(71),
  [247] = {.entry = {.count = 1, .reusable = true}}, SHIFT(64),
  [249] = {.entry = {.count = 1, .reusable = false}}, SHIFT(75),
  [251] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pi_repeat1, 2), SHIFT_REPEAT(77),
  [254] = {.entry = {.count = 1, .reusable = true}}, SHIFT(19),
  [256] = {.entry = {.count = 1, .reusable = false}}, SHIFT(85),
  [258] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_cdata, 1),
  [260] = {.entry = {.count = 1, .reusable = false}}, SHIFT(77),
  [262] = {.entry = {.count = 1, .reusable = true}}, SHIFT(57),
  [264] = {.entry = {.count = 1, .reusable = false}}, SHIFT(72),
  [266] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pi_repeat1, 2), SHIFT_REPEAT(81),
  [269] = {.entry = {.count = 1, .reusable = true}}, SHIFT(22),
  [271] = {.entry = {.count = 1, .reusable = false}}, SHIFT(83),
  [273] = {.entry = {.count = 1, .reusable = true}}, SHIFT(30),
  [275] = {.entry = {.count = 1, .reusable = true}}, SHIFT(123),
  [277] = {.entry = {.count = 1, .reusable = true}}, SHIFT(41),
  [279] = {.entry = {.count = 1, .reusable = true}}, SHIFT(37),
  [281] = {.entry = {.count = 1, .reusable = true}}, SHIFT(43),
  [283] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_attr, 3, .production_id = 5),
  [285] = {.entry = {.count = 1, .reusable = true}}, SHIFT(143),
  [287] = {.entry = {.count = 1, .reusable = true}}, SHIFT(96),
  [289] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_attribute, 1),
  [291] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_attr_value, 2),
  [293] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_ns_decl, 5, .production_id = 7),
  [295] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_attr, 5, .production_id = 8),
  [297] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_attr_value, 3),
  [299] = {.entry = {.count = 1, .reusable = true}}, SHIFT(106),
  [301] = {.entry = {.count = 1, .reusable = true}}, SHIFT(84),
  [303] = {.entry = {.count = 1, .reusable = true}}, SHIFT(125),
  [305] = {.entry = {.count = 1, .reusable = true}}, SHIFT(103),
  [307] = {.entry = {.count = 1, .reusable = true}}, SHIFT(126),
  [309] = {.entry = {.count = 1, .reusable = true}}, SHIFT(56),
  [311] = {.entry = {.count = 1, .reusable = true}}, SHIFT(139),
  [313] = {.entry = {.count = 1, .reusable = true}}, SHIFT(124),
  [315] = {.entry = {.count = 1, .reusable = true}}, SHIFT(63),
  [317] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_encoding_value, 3),
  [319] = {.entry = {.count = 1, .reusable = true}}, SHIFT(51),
  [321] = {.entry = {.count = 1, .reusable = true}}, SHIFT(80),
  [323] = {.entry = {.count = 1, .reusable = true}}, SHIFT(59),
  [325] = {.entry = {.count = 1, .reusable = true}}, SHIFT(48),
  [327] = {.entry = {.count = 1, .reusable = true}}, SHIFT(49),
  [329] = {.entry = {.count = 1, .reusable = true}}, SHIFT(109),
  [331] = {.entry = {.count = 1, .reusable = true}}, SHIFT(142),
  [333] = {.entry = {.count = 1, .reusable = true}}, SHIFT(112),
  [335] = {.entry = {.count = 1, .reusable = true}}, SHIFT(58),
  [337] = {.entry = {.count = 1, .reusable = true}}, SHIFT(114),
  [339] = {.entry = {.count = 1, .reusable = true}},  ACCEPT_INPUT(),
  [341] = {.entry = {.count = 1, .reusable = true}}, SHIFT(32),
  [343] = {.entry = {.count = 1, .reusable = true}}, SHIFT(138),
  [345] = {.entry = {.count = 1, .reusable = true}}, SHIFT(42),
  [347] = {.entry = {.count = 1, .reusable = true}}, SHIFT(61),
  [349] = {.entry = {.count = 1, .reusable = true}}, SHIFT(119),
  [351] = {.entry = {.count = 1, .reusable = true}}, SHIFT(113),
  [353] = {.entry = {.count = 1, .reusable = true}}, SHIFT(54),
  [355] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_encoding, 4, .production_id = 6),
  [357] = {.entry = {.count = 1, .reusable = true}}, SHIFT(62),
  [359] = {.entry = {.count = 1, .reusable = true}}, SHIFT(68),
  [361] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_version, 4, .production_id = 4),
  [363] = {.entry = {.count = 1, .reusable = true}}, SHIFT(107),
  [365] = {.entry = {.count = 1, .reusable = true}}, SHIFT(25),
  [367] = {.entry = {.count = 1, .reusable = true}}, SHIFT(20),
  [369] = {.entry = {.count = 1, .reusable = true}}, SHIFT(78),
  [371] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_decl, 5, .production_id = 2),
  [373] = {.entry = {.count = 1, .reusable = true}}, SHIFT(100),
  [375] = {.entry = {.count = 1, .reusable = true}}, SHIFT(47),
  [377] = {.entry = {.count = 1, .reusable = true}}, SHIFT(33),
  [379] = {.entry = {.count = 1, .reusable = true}}, SHIFT(73),
  [381] = {.entry = {.count = 1, .reusable = true}}, SHIFT(35),
  [383] = {.entry = {.count = 1, .reusable = true}}, SHIFT(133),
  [385] = {.entry = {.count = 1, .reusable = true}}, SHIFT(36),
  [387] = {.entry = {.count = 1, .reusable = true}}, SHIFT(102),
  [389] = {.entry = {.count = 1, .reusable = true}}, SHIFT(38),
  [391] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_version_value, 3),
  [393] = {.entry = {.count = 1, .reusable = true}}, SHIFT(115),
  [395] = {.entry = {.count = 1, .reusable = true}}, SHIFT(127),
  [397] = {.entry = {.count = 1, .reusable = true}}, SHIFT(55),
  [399] = {.entry = {.count = 1, .reusable = true}}, SHIFT(117),
  [401] = {.entry = {.count = 1, .reusable = true}}, SHIFT(130),
  [403] = {.entry = {.count = 1, .reusable = true}}, SHIFT(70),
  [405] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_decl, 6, .production_id = 2),
  [407] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_xml_decl, 4, .production_id = 2),
};

#ifdef __cplusplus
extern "C" {
#endif
#ifdef _WIN32
#define TS_PUBLIC __declspec(dllexport)
#else
#define TS_PUBLIC __attribute__((visibility("default")))
#endif

TS_PUBLIC const TSLanguage *tree_sitter_xml() {
  static const TSLanguage language = {
    .version = LANGUAGE_VERSION,
    .symbol_count = SYMBOL_COUNT,
    .alias_count = ALIAS_COUNT,
    .token_count = TOKEN_COUNT,
    .external_token_count = EXTERNAL_TOKEN_COUNT,
    .state_count = STATE_COUNT,
    .large_state_count = LARGE_STATE_COUNT,
    .production_id_count = PRODUCTION_ID_COUNT,
    .field_count = FIELD_COUNT,
    .max_alias_sequence_length = MAX_ALIAS_SEQUENCE_LENGTH,
    .parse_table = &ts_parse_table[0][0],
    .small_parse_table = ts_small_parse_table,
    .small_parse_table_map = ts_small_parse_table_map,
    .parse_actions = ts_parse_actions,
    .symbol_names = ts_symbol_names,
    .field_names = ts_field_names,
    .field_map_slices = ts_field_map_slices,
    .field_map_entries = ts_field_map_entries,
    .symbol_metadata = ts_symbol_metadata,
    .public_symbol_map = ts_symbol_map,
    .alias_map = ts_non_terminal_alias_map,
    .alias_sequences = &ts_alias_sequences[0][0],
    .lex_modes = ts_lex_modes,
    .lex_fn = ts_lex,
    .primary_state_ids = ts_primary_state_ids,
  };
  return &language;
}
#ifdef __cplusplus
}
#endif
