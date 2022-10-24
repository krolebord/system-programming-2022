package lab4;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final LexemePattern[] lexemes;

    public Lexer(LexemePattern[] lexemes) {
        this.lexemes = lexemes;
    }

    public static class LexemeResult {
        public final int start;
        public final LexemeType type;
        public final CharSequence text;

        private LexemeResult(int start, LexemeType type, CharSequence text) {
            this.start = start;
            this.type = type;
            this.text = text;
        }
    }

    private LexemeResult readToken(CharSequence input, int start) throws Exception {
        for (var lexeme : lexemes) {
            var slice = CharBuffer.wrap(input, start, input.length());
            var matcher = lexeme.pattern.matcher(slice);
            if (!matcher.find()) {
                continue;
            }
            return new LexemeResult(start, lexeme.type, slice.subSequence(matcher.start(), matcher.end()));
        }
        throw new Exception("Exhausted input while lexing token");
    }

    public List<LexemeResult> process(CharSequence input) throws Exception {
        var lexemes = new ArrayList<LexemeResult>();
        for (var i = 0; i < input.length();) {
            var result = readToken(input, i);
            lexemes.add(result);
            i = result.start + result.text.length();
        }
        return lexemes;
    }
}
