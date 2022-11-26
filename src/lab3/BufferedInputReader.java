package lab3;

import java.io.IOException;
import java.io.Reader;

public class BufferedInputReader implements InputReader {
    private final Reader reader;

    private int next;
    private int position = -1;
    private StringBuilder buffer;

    public BufferedInputReader(Reader reader) throws IOException {
        this.reader = reader;
        this.buffer = new StringBuilder();
        next = reader.read();
    }

    public boolean hasNext() {
        return next != -1;
    }

    public char next() throws IOException, ReaderExhaustedException {
        if (!hasNext())
            throw new ReaderExhaustedException();

        var curr = (char) next;
        next = reader.read();

        position++;
        buffer.append(curr);
        return curr;
    }

    public char peek() {
        return (char) next;
    }

    public int position() {
        return position;
    }

    public String flushBuffer() {
        var s = buffer.toString();
        buffer.setLength(0);
        return s;
    }
}
