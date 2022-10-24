package lab4;

public class GoLexemes {
    public static String[] keywords = new String[] {
            "break",
            "case",
            "chan",
            "const",
            "continue",
            "default",
            "defer",
            "else",
            "fallthrough",
            "for",
            "func",
            "go",
            "goto",
            "if",
            "import",
            "interface",
            "map",
            "package",
            "range",
            "return",
            "select",
            "struct",
            "switch",
            "type",
            "var"
    };

    public static String[] operators = new String[] {
            "+", "-", "*", "/", "%",
            "&", "|", "^", "<<", ">>", "&^",
            "+=", "-=", "*=", "/=", "%=",
            "&=", "|=", "^=", "<<=", ">>=", "&^=",
            "&&", "||", "<-", "++", "--",
            "==", "<", ">", "=", "!", "~",
            "!=", "<=", ">=", ":=", "...",
            "[", "]",
            ",", ".", ":"
    };

    public static LexemePattern[] patterns = new LexemePattern[] {
            // Single line comment //
            new LexemePattern("^//.*", LexemeType.SingleLineComment),
            // Multiline comments //
            new LexemePattern("^/\\*(.|\\n|\\r)+?\\*/", LexemeType.MultiLineComment),

            // Space
            new LexemePattern("^\\s+", LexemeType.Space),

            // Keywords
            new LexemePattern("^(?<!\\w)(" + String.join("|", keywords) + ")(?<!\\w)?", LexemeType.Keyword),

            // Bool literal
            new LexemePattern("^(?<!\\w)(true|false)(?<!\\w)?", LexemeType.BoolLiteral),

            // Identifier
            new LexemePattern("^[a-zA-Z_]\\w*", LexemeType.Identifier),

            // Delimiters
            new LexemePattern("^[\\(\\)\\{\\}\\;]", LexemeType.Delimiter),

            // Complex number literal
            new LexemePattern("^([\\+\\-]?([0-9]+([\\.][0-9]*)?|[\\.][0-9]+))( *[\\+\\-] *)([\\+\\-]?([0-9]+([\\.][0-9]*)?|[\\.][0-9]+))i", LexemeType.ComplexNumberLiteral),
            // Number literal
            new LexemePattern("^[\\+\\-]?([0-9]+([\\.][0-9]*)?|[\\.][0-9]+)", LexemeType.NumberLiteral),

            // Operators
            new LexemePattern("^(?<!\\w)(\\Q" + String.join("\\E|\\Q", operators) + "\\E)(?<!\\w)?", LexemeType.Operator),

            // Char literal (Rune)
            new LexemePattern("^'(.|\\\\.)'", LexemeType.CharLiteral),

            // String literal
            new LexemePattern("^\\\"([^\\\"\\\\\\n\\r]|\\\\.)*\\\"", LexemeType.StringLiteral),
            // Raw string literal
            new LexemePattern("^`[^`]*`", LexemeType.RawStringLiteral),

            // Other
            new LexemePattern("^[\\s\\S]", LexemeType.Unknown)
    };
}
