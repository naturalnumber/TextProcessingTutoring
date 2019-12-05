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

import java.io.*;
import java.util.StringTokenizer;

public class MyKattioByWords extends MyKattio {

    public MyKattioByWords() {
    }

    public MyKattioByWords(InputStream i) {
        super(i);
    }

    public MyKattioByWords(OutputStream o) {
        super(o);
    }

    public MyKattioByWords(InputStream i, OutputStream o) {
        super(i, o);
    }

    public MyKattioByWords(boolean testing) {
        super(testing);
    }

    public MyKattioByWords(boolean testing, OutputStream o) {
        super(testing, o);
    }

    public MyKattioByWords(boolean testing, String path) {
        super(testing, path);
    }

    public MyKattioByWords(boolean testing, String path, OutputStream o) {
        super(testing, path, o);
    }

    public String getWord() {
        return nextToken();
    }

    protected String line;
    protected StringTokenizer st;

    protected String peekToken() {
        if (token == null)
            try {
                while (st == null || !st.hasMoreTokens()) {
                    line = r.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                }
                token = st.nextToken();
            } catch (IOException e) { e.printStackTrace(); }
        return token;
    }
}
