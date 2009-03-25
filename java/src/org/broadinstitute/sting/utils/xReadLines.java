package org.broadinstitute.sting.utils;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.*;

/**
 * Support for Python-like xreadlines() function as a class.  This is an iterator and iterable over
 * Strings, each corresponding a line in the file (minus newline).  Enables the very simple accessing
 * of lines in a file as:
 *
 * xReadLines reader = new xReadLines(new File(file_name));
 * List<String> lines = reader.readLines();
 * reader.close();
 *
 * or
 *
 * for ( String line : new xReadLines(new File(file_name)) {
 *   doSomeWork(line);
 * }
 *
 * For the love of god, please use this system for reading lines in a file.
 */
public class xReadLines implements Iterator<String>, Iterable<String> {
    private BufferedReader in;          // The stream we're reading from
    private String nextline = null;     // Return value of next call to next()
    private boolean trimWhitespace = true;

    /**
     * Creates a new xReadLines object to read lines from filename
     *
     * @param filename
     * @throws FileNotFoundException
     */
    public xReadLines(final File filename, final boolean trimWhitespace) throws FileNotFoundException {
        this(new FileReader(filename), trimWhitespace);
    }

    public xReadLines(final File filename) throws FileNotFoundException {
        this(filename, true);
    }

    /**
     * Creates a new xReadLines object to read lines from fileReader
     *
     * @param fileReader
     * @throws FileNotFoundException
     */
    public xReadLines(final FileReader fileReader, final boolean trimWhitespace) throws FileNotFoundException {
        this(new BufferedReader(fileReader), trimWhitespace);
    }

    public xReadLines(final FileReader fileReader) throws FileNotFoundException {
        this(fileReader, true);
    }

    /**
     * Creates a new xReadLines object to read lines from an input stream
     *
     * @param inputStream
     * @throws FileNotFoundException
     */
    public xReadLines(final InputStream inputStream, final boolean trimWhitespace) throws FileNotFoundException {
        this(new BufferedReader(new InputStreamReader(inputStream)), trimWhitespace);
    }

    public xReadLines(final InputStream inputStream) throws FileNotFoundException {
        this(inputStream, true);
    }


    /**
     * Creates a new xReadLines object to read lines from an bufferedReader
     *
     * @param reader
     * @throws FileNotFoundException
     */
    public xReadLines(final BufferedReader reader, final boolean trimWhitespace) throws FileNotFoundException {
        try {
            this.in = reader;
            nextline = readNextLine();
            this.trimWhitespace = trimWhitespace;
        } catch(IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public xReadLines(final BufferedReader reader) throws FileNotFoundException {
        this(reader, true);
    }

    /**
     * Reads all of the lines in the file, and returns them as a list of strings
     *
     * @return
     */
    public List<String> readLines() {
        List<String> lines = new LinkedList<String>();
        for ( String line : this ) {
            lines.add(line);
        }
        return lines;
    }

    /**
     * I'm an iterator too...
     * @return
     */
    public Iterator<String> iterator() {
        return this;
    }

    public boolean hasNext() {
        return nextline != null;
    }

    /**
     * Actually reads the next line from the stream, not accessible publically
     * @return
     */
    private String readNextLine() throws IOException {
        String nextline = in.readLine();   // Read another line
        if (nextline != null && trimWhitespace )
            nextline = nextline.trim();
        return nextline;
    }

    /**
     * Returns the next line (minus whitespace) 
     * @return
     */
    public String next() {
        try {
            String result = nextline;
            nextline = readNextLine();

            // If we haven't reached EOF yet
            if (nextline == null) {
                in.close();             // And close on EOF
            }

            // Return the line we read last time through.
            return result;
        } catch(IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // The file is read-only; we don't allow lines to be removed.
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close() throws IOException {
        this.in.close();
    }
}
