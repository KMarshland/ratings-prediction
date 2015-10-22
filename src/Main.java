import java.util.HashMap;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Main {

    public static void main(String[] args){

        loadAll();

        int sampleSize = 50;

        //start it with equal weights
        Predictor champion = new Predictor(new double[]{2.57, 0.69, -0.41, 1.0, 0.09, 3.82, 1.82, 2.17, 4.03});
        double championResult = champion.test(sampleSize);

        System.out.println("Initial result: " + championResult);

        int testedPredictors = 0;

        long startTime = System.currentTimeMillis();

        //keep on going until it has an acceptable accuracy
        while (championResult < 1000) {
            //make it recalculate the champion
            championResult = champion.test(sampleSize);

            for (Predictor child : champion.getChildren()) {
                double childResult = child.test(sampleSize);
                if (Predictor.testMode == Predictor.TestMode.Accuracy ?
                        (childResult > championResult) :
                        (childResult < championResult)
                ) {//if it had greater accuracy
                    champion = child;
                    championResult = childResult;
                }
                testedPredictors ++;
            }

            //give the champion so far
            System.out.println("" +
                    (Predictor.testMode == Predictor.TestMode.Accuracy ?
                                "Accuracy: " + Math.round(championResult * 1000)/10.0 + "%" :
                                "Error: " + championResult) + "; "  +
                            "Average time per test: " + ((System.currentTimeMillis() - startTime) / testedPredictors) + "ms; " +
                            "Current champion: " + champion.stringifyWeights()
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
