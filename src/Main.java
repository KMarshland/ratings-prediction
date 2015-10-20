import java.util.HashMap;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Main {

    public static void main(String[] args){

        loadAll();

        int sampleSize = 50;

        HashMap<String, Double> cachedAccuracies = new HashMap<>();

        //start it with equal weights
        Predictor champion = new Predictor(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        double championError = champion.test(sampleSize);
        cachedAccuracies.put(champion.stringifyWeights(), championError); //save the champion

        int testedPredictors = 0;

        long startTime = System.currentTimeMillis();

        //keep on going until it has an acceptable accuracy
        while (championError > -1) {
            for (Predictor child : champion.getChildren()) {
                double childError;
                if (false && cachedAccuracies.containsKey(child.stringifyWeights())){
                    childError = cachedAccuracies.get(child.stringifyWeights());
                } else {
                    childError = child.test(sampleSize);
//                    System.out.println("Child error: " + childError);
                    cachedAccuracies.put(child.stringifyWeights(), childError);
                }
                if (childError < championError) {
                    champion = child;
                    championError = childError;
//                    System.out.println("New champion: " + childError);
                }
                testedPredictors ++;
            }

            //give the champion so far
            System.out.println("Error: " + championError + "; average time per test: " +
                            ((System.currentTimeMillis() - startTime) / testedPredictors) + "ms; " +
                            " current champion: " + champion.stringifyWeights()
            );
        }

        System.out.println((System.currentTimeMillis() - startTime) + "ms to test");

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
