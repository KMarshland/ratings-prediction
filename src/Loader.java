/**
 * Created by kaimarshland on 10/14/15.
 */
import java.util.function.Function;
import java.io.*;

public class Loader {

    public Loader(String source, Function<String[], String> loadLine) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            String currentLine = reader.readLine();//skip reading the first line
            while ((currentLine = reader.readLine()) != null) {
                loadLine.apply(currentLine.split("\t"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
