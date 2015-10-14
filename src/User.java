/**
 * Created by kaimarshland on 10/14/15.
 */

import java.util.function.Function;

public class User {

    public static void load(String source){
        new Loader(source, new Function<String[], String>() {
            public String apply(String[] parts) {
                System.out.println(parts[0]);
                return "";
            }
        });
    }
}
