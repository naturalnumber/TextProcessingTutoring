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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public abstract class MyKattio extends PrintWriter {

    public static String path = "./Solutions/src";

    protected BufferedReader r;
    protected String token;

    public MyKattio() {
        this(System.in, System.out);
    }
    public MyKattio(InputStream i) {
        this(i, System.out);
    }
    public MyKattio(OutputStream o) {
        this(System.in, o);
    }
    public MyKattio(InputStream i, OutputStream o) {
        super(new BufferedOutputStream(o));
        r = new BufferedReader(new InputStreamReader(i));
    }

    public MyKattio(boolean testing) {
        this(testing, System.out);
    }
    public MyKattio(boolean testing, OutputStream o) {
        super(new BufferedOutputStream(o));
        r = new BufferedReader(new InputStreamReader((testing) ? getFileInputStream() : System.in));
    }

    public MyKattio(boolean testing, String path) {
        this(testing, path, System.out);
    }
    public MyKattio(boolean testing, String path, OutputStream o) {
        super(new BufferedOutputStream(o));
        this.path = path;
        r = new BufferedReader(new InputStreamReader((testing) ? getFileInputStream() : System.in));
    }

    private static FileInputStream getFileInputStream() {
        boolean check = true;
        FileInputStream fileIn = null;
        while (check) try {
            JFileChooser chooser = new JFileChooser(path);
            chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int n = chooser.showOpenDialog(null);
            if (n == JFileChooser.APPROVE_OPTION) {
                fileIn = new FileInputStream(chooser.getSelectedFile());
                check = false;
            }
            if (n == JFileChooser.CANCEL_OPTION) System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileIn;
    }

    @Override
    public void close() {
        super.close();
        try {
            r.close();
        } catch (Exception e) {}
    }

    public boolean hasMore() {
        return peekToken() != null;
    }

    public int getInt() {
        return Integer.parseInt(nextToken());
    }

    public double getDouble() {
        return Double.parseDouble(nextToken());
    }

    public long getLong() {
        return Long.parseLong(nextToken());
    }

    public String getNext() {
        return nextToken();
    }

    protected String nextToken() {
        String ans = peekToken();
        token = null;
        return ans;
    }

    //  Abstract methods

    protected abstract String peekToken();
}
