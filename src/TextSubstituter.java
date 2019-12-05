import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

// An example use is included in text_replace_example.txt
public class TextSubstituter {
    private static final String[] ENDS = {".", "!", "?", ",", ":", ";"};
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();

        if (args.length == 1) {
            try (var in = new BufferedReader(new FileReader(args[0]))) {
                in.lines().forEach(l -> {
                    try {
                        var ls = l.split(" ");
                        map.put(ls[0], ls[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Unable to parse line: " + l);
                    }
                });
            } catch (FileNotFoundException e) {
                System.err.println("Could not locate pair file: " + args[0]);
            } catch (IOException e) {
                System.err.println("Error occurred opening pair file " + args[0]);
                e.printStackTrace();
            }
        } else if (args.length == 0) {
            System.out.println("No change pairs given.");
        } else {
            System.out.println("Too many arguments given.");
        }

        try (var in = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer st;
            String word, end;
            while (in.ready()) {
                st = new StringTokenizer(in.readLine()); // Break into words
                while (st.hasMoreTokens()) {
                    word = st.nextToken();
                    end = "";
                    for (var e : ENDS)
                        if (word.endsWith(e)) {
                            word = word.substring(0, word.length()-e.length()); // Trim off end character
                            end = e;
                            break;
                        }

                    System.out.print(map.getOrDefault(word, word));
                    System.out.print(end);
                    if (st.hasMoreTokens()) System.out.print(' ');
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("Unexpected error occurred...");
            e.printStackTrace();
        }
    }
}
