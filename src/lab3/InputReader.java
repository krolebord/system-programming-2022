package lab3;

import java.io.IOException;

public interface InputReader {
    public static class ReaderExhaustedException extends Exception {}

    public boolean hasNext();

    public char next() throws IOException, ReaderExhaustedException;

    public char peek();
}
