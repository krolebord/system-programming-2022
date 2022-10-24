package lab4;

import java.util.regex.Pattern;

public class LexemePattern {
    public final Pattern pattern;
    public final LexemeType type;

    public LexemePattern(String pattern, LexemeType type) {
        this.pattern = Pattern.compile(pattern);
        this.type = type;
    }
}
