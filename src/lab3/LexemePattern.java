package lab3;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

public class LexemePattern {
    public interface Matcher {
        boolean match(char c);
    }
    public interface LexemeGetter {
        LexemeType getLexeme(char c, InputReader reader) throws IOException, InputReader.ReaderExhaustedException;
    }

    public final Matcher matcher;
    public final LexemeGetter getter;

    public LexemePattern(Matcher matcher, LexemeGetter getter) {
        this.matcher = matcher;
        this.getter = getter;
    }

    public static LexemeType MatchPatterns(InputReader reader, LexemePattern[] patterns) throws IOException, InputReader.ReaderExhaustedException {
        char c = reader.next();

        for (var pattern : patterns) {
            if (pattern.matcher.match(c)) {
                try {
                    return pattern.getter.getLexeme(c, reader);
                }
                catch (InputReader.ReaderExhaustedException e) {
                    return LexemeType.Unknown;
                }
            }
        }
        return LexemeType.Unknown;
    }
}
