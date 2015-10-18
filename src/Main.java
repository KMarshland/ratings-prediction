/**
 * Created by kaimarshland on 10/14/15.
 */
public class Main {

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        loadAll();
        System.out.println((System.currentTimeMillis() - startTime) + "ms to start");

    }

    //loads everything and establishes relations
    static void loadAll(){
        User.load("data/users.tsv");
        Movie.load("data/movies.tsv");
        Rating.load("data/ratings.tsv");

        //build the has/belongs to relationships
        Rating.connectAll();
    }

    public static String longestSubstring(String smaller, String larger){
        //take shorter and shorter substrings (n=0 is just the word, n=1 is substrings 1 character shorter than the word, etc)
        for (int n = 0; n < smaller.length(); n++) {
            //loop through all substrings with that length
            for (int i = 0; i <= n; i++) {
                if (larger.contains(smaller.substring(i, i + smaller.length() - n))) {//see if they're contained by the larger word
                    return smaller.substring(i, i + smaller.length() - n);
                }
            }
        }

        return "";
    }
}
