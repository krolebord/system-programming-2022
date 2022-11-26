package lab3;

import java.util.*;

public class GoLexemes {
    public static class Constants {
        public static HashSet<String> keywords = new HashSet<>() {{
            add("break");
            add("case");
            add("chan");
            add("const");
            add("continue");
            add("default");
            add("defer");
            add("else");
            add("fallthrough");
            add("for");
            add("func");
            add("goto");
            add("go");
            add("if");
            add("import");
            add("interface");
            add("map");
            add("package");
            add("range");
            add("return");
            add("select");
            add("struct");
            add("switch");
            add("type");
            add("var");
        }};

        public static List<HashSet<Character>> operatorSymbols = Symbols.getOperatorSymbols(new String[] {
                "+=", "-=", "*=", "/=", "%=",
                "&=", "|=", "^=", "<<=", ">>=", "&^=",
                "&&", "||", "<-", "++", "--",
                "==", "<", ">", "=", "~",
                "!=", "<=", ">=", ":=", "...",
                "&", "|", "^", "<<", ">>", "&^",
                "!", "+", "-", "*", "/", "%",
                "[", "]", "{", "}", "(", ")",
                ",", ".", ":", ";"
        });
    }

    public static LexemePattern[] patterns = new LexemePattern[] {
            // Space
            new LexemePattern(
                    c -> Symbols.isSpace(c) || Symbols.isNewLine(c),
                    (c, reader) -> LexemeType.Space
            ),
            // Char Literal
            new LexemePattern(
                    c -> c == '\'',
                    (c, reader) -> {
                        var charVal = reader.next();
                        if (charVal == '\'')
                            return LexemeType.Unknown;

                        if (charVal == '\\')
                            reader.next();

                        var closingQuote = reader.next();
                        if (closingQuote == '\'')
                            return LexemeType.CharLiteral;

                        return LexemeType.Unknown;
                    }
            ),
            // String Literal
            new LexemePattern(
                    c -> c == '"',
                    (c, reader) -> {
                        while(reader.hasNext()) {
                            c = reader.next();
                            if (c == '"') {
                                return LexemeType.StringLiteral;
                            }
                            if (Symbols.isNewLine(c)) {
                                return LexemeType.Unknown;
                            }
                        }
                        return LexemeType.Unknown;
                    }
            ),
            // Raw String Literal
            new LexemePattern(
                    c -> c == '`',
                    (c, reader) -> {
                        while(reader.hasNext()) {
                            c = reader.next();
                            if (c == '`') {
                                return LexemeType.RawStringLiteral;
                            }
                        }
                        return LexemeType.Unknown;
                    }
            ),
            // /
            new LexemePattern(
                    c -> c == '/',
                    (bracket, reader) -> {
                        // Single Line Comment
                        if (reader.peek() == '/') {
                            reader.next();
                            while (reader.hasNext()) {
                                if (Symbols.isNewLine(reader.next())) {
                                    return LexemeType.SingleLineComment;
                                }
                            }
                            return LexemeType.Unknown;
                        }
                        // Multi Line Comment
                        if (reader.peek() == '*') {
                            while (reader.hasNext()) {
                                if (reader.next() == '*' && reader.next() == '/') {
                                    return LexemeType.MultiLineComment;
                                }
                            }
                            return LexemeType.Unknown;
                        }
                        return LexemeType.Operator;
                    }
            ),
            // Number Literal
            new LexemePattern(
                    Symbols::isNumeric,
                    (c, reader) -> {
                        var isDecimal = true;
                        while (reader.hasNext()) {
                            if (reader.peek() == 'i') {
                                reader.next();
                                return LexemeType.NumberLiteral;
                            }
                            if (reader.peek() == '.') {
                                reader.next();
                                if (!isDecimal) return LexemeType.Unknown;
                                else isDecimal = false;
                            }
                            if (Symbols.isNumeric(reader.peek()) || reader.peek() == 'e') {
                                reader.next();
                                continue;
                            }
                            if (Symbols.isLetter(reader.peek())) {
                                return LexemeType.Unknown;
                            }
                            return LexemeType.NumberLiteral;
                        }
                        return LexemeType.Unknown;
                    }
            ),
            // Identifier
            new LexemePattern(
                    Symbols::isIdentifierSymbol,
                    (c, reader) -> {
                        var identifierBuffer = new StringBuilder();
                        identifierBuffer.append(c);
                        while (reader.hasNext()) {
                            if (Symbols.isIdentifierSymbol(reader.peek()) || Symbols.isNumeric(reader.peek())) {
                                identifierBuffer.append(reader.next());
                                continue;
                            }
                            var identifier = identifierBuffer.toString();
                            var isKeyword = Constants.keywords.contains(identifier);
                            return isKeyword ? LexemeType.Keyword : LexemeType.Identifier;
                        }
                        return LexemeType.Unknown;
                    }
            ),
            // Operator
            new LexemePattern(
                    c -> Constants.operatorSymbols.get(0).contains(c),
                    (c, reader) -> {
                        for (int i = 1; i < Constants.operatorSymbols.size() && Constants.operatorSymbols.get(i).contains(reader.peek()); ++i) {
                            reader.next();
                        }
                        return LexemeType.Operator;
                    }
            )
    };
}
