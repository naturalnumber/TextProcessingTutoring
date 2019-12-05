import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

// Examples of instantiation
public class IOHelper {

    /**
     * How to make a Reader from a file.
     *
     * Uses a BufferedReader to help make io more efficient by buffering input.
     *
     * Buffering means when it reads from the underlying FileReader it will read
     * whatever is there and hold on the the extra until you ask for it. This means
     * fewer file reads, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param toRead The File
     *
     * @return The Reader
     *
     * @throws FileNotFoundException From FileReader
     */
    public static Reader makeReader(File toRead) throws FileNotFoundException {
        return new BufferedReader(new FileReader(toRead));
    }

    /**
     * How to make a Reader from an InputStream.
     *
     * Uses a BufferedReader to help make io more efficient by buffering input.
     *
     * Buffering means when it reads from the underlying FileReader it will read
     * whatever is there and hold on the the extra until you ask for it. This means
     * fewer file reads, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param toRead The InputStream
     *
     * @return The Reader
     */
    public static Reader makeReader(InputStream toRead) {
        return new BufferedReader(new InputStreamReader(toRead));
    }

    /**
     * How to make a Reader from a String.
     * You would want to do this if you had a long string you wanted to read like a file.
     *
     * Uses a BufferedReader to help make io more efficient by buffering input.
     *
     * Buffering means when it reads from the underlying FileReader it will read
     * whatever is there and hold on the the extra until you ask for it. This means
     * fewer file reads, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param toRead The String
     *
     * @return The Reader
     */
    public static Reader makeReader(String toRead) {
        return new BufferedReader(new StringReader(toRead));
    }

    /**
     * How to make a Writer to a File.
     * Writers aren't actually very useful, so usually we use PrintWriters.
     *
     * Uses a BufferedWriter to help make io more efficient by buffering output.
     *
     * Buffering means when you write to the Writer it will hold on to what you wrote
     * until flush is called or its buffer is full. This means fewer writes, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param writeTo The File
     *
     * @return The Writer
     *
     * @throws IOException From FileWriter
     */
    public static Writer makeWriter(File writeTo) throws IOException {
        return new BufferedWriter(new FileWriter(writeTo));
    }

    /**
     * How to make a Writer to an OutputStream.
     * Writers aren't actually very useful, so usually we use PrintWriters.
     *
     * Uses a BufferedWriter to help make io more efficient by buffering output.
     *
     * Buffering means when you write to the Writer it will hold on to what you wrote
     * until flush is called or its buffer is full. This means fewer writes, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param writeTo The OutputStream
     *
     * @return The Writer
     */
    public static Writer makeWriter(OutputStream writeTo) {
        return new BufferedWriter(new OutputStreamWriter(writeTo));
    }

    /**
     * How to make a Writer to a String.
     * Use toString() to get the string value.
     * Writers aren't actually very useful, so usually we use PrintWriters.
     *
     * Uses a BufferedWriter to help make io more efficient by buffering output.
     *
     * Buffering means when you write to the Writer it will hold on to what you wrote
     * until flush is called or its buffer is full. This means fewer writes, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @return The Writer
     */
    public static Writer makeWriter() {
        return new BufferedWriter(new StringWriter());
    }

    /**
     * How to make a PrintWriter to a File.
     *
     * Uses a BufferedWriter to help make io more efficient by buffering output.
     *
     * Buffering means when you write to the Writer it will hold on to what you wrote
     * until flush is called or its buffer is full. This means fewer writes, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param writeTo The File
     *
     * @return The Writer
     *
     * @throws IOException From FileWriter
     */
    public static PrintWriter makePrintWriter(File writeTo) throws IOException {
        return new PrintWriter(makeWriter(writeTo));
    }

    /**
     * How to make a PrintWriter to an OutputStream.
     *
     * Uses a BufferedWriter to help make io more efficient by buffering output.
     *
     * Buffering means when you write to the Writer it will hold on to what you wrote
     * until flush is called or its buffer is full. This means fewer writes, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @param writeTo The OutputStream
     *
     * @return The Writer
     */
    public static PrintWriter makePrintWriter(OutputStream writeTo) {
        return new PrintWriter(makeWriter(writeTo));
    }

    /**
     * How to make a PrintWriter to a String.
     * Use toString() to get the string value.
     *
     * Uses a BufferedWriter to help make io more efficient by buffering output.
     *
     * Buffering means when you write to the Writer it will hold on to what you wrote
     * until flush is called or its buffer is full. This means fewer writes, which are slow.
     *
     * This, like most io classes throw IOExceptions that may need to be checked for.
     *
     * @return The Writer
     */
    public static PrintWriter makePrintWriter() {
        return new PrintWriter(makeWriter());
    }
}
