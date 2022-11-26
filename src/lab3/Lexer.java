package lab3;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;

public class Lexer {
    private final BufferedInputReader reader;
    private final LexemePattern[] patterns;

    public Lexer(Reader reader, LexemePattern[] patterns) throws IOException {
        this.reader = new BufferedInputReader(reader);
        this.patterns = patterns;
    }

    public static class LexemeResult {
        public final int start;
        public final String text;
        public final LexemeType type;

        private LexemeResult(int start, String text, LexemeType type) {
            this.start = start;
            this.text = text;
            this.type = type;
        }
    }

    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    public LexemeResult getNextLexeme() throws IOException {
        var start = reader.position();
        try {
            var lexemeType = LexemePattern.MatchPatterns(reader, patterns);
            return new LexemeResult(start, reader.flushBuffer(), lexemeType);
        } catch (InputReader.ReaderExhaustedException e) {
            throw new EOFException();
        }
    }

    public Stream<LexemeResult> stream() {
        Stream<Lexer.LexemeResult> stream = Stream.iterate(
                null,
                x -> {
                    try {
                        return hasNext();
                    } catch (IOException e) {
                        return false;
                    }
                },
                x -> {
                    try {
                        return getNextLexeme();
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
        );
        return stream.skip(1);
    }
}
