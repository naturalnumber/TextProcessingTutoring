package arc.IO; /** Simple yet moderately fast I/O routines.
 *
 * Example usage:
 *
 * Kattio io = new Kattio(System.in, System.out);
 *
 * while (io.hasMore()) {
 *    int n = io.getInt();
 *    double d = io.getDouble();
 *    double ans = d*n;
 *
 *    io.println("Answer: " + ans);
 * }
 *
 * io.close();
 *
 *
 * Some notes:
 *
 * - When done, you should always do io.close() or io.flush() on the
 *   Kattio-instance, otherwise, you may lose output.
 *
 * - The getInt(), getDouble(), and getLong() methods will throw an
 *   exception if there is no more data in the input, so it is generally
 *   a good idea to use hasMore() to check for end-of-file.
 *
 * @author: Kattis
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyKattioByLines extends MyKattio {

    public MyKattioByLines() {}

    public MyKattioByLines(InputStream i) {
        super(i);
    }

    public MyKattioByLines(OutputStream o) {
        super(o);
    }

    public MyKattioByLines(InputStream i, OutputStream o) {
        super(i, o);
    }

    public MyKattioByLines(boolean testing) {
        super(testing);
    }

    public MyKattioByLines(boolean testing, OutputStream o) {
        super(testing, o);
    }

    public MyKattioByLines(boolean testing, String path) {
        super(testing, path);
    }

    public MyKattioByLines(boolean testing, String path, OutputStream o) {
        super(testing, path, o);
    }

    public String[] getWords() {
        String s = nextToken();
        return (s != null) ? s.split(" ") : null;
    }

    protected String peekToken() {
        if (token == null) try {
            token = r.readLine();
        } catch (IOException e) { e.printStackTrace(); }
        return token;
    }
}
