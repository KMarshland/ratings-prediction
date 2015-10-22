import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Main {

    public static void main(String[] args){

        //make it load the existing ratings from the files
        loadAll();

        //start it with the given weights
        Predictor champion = new Predictor(new double[]{2.57, 0.38, 0, 0, 0.96, 3.36, 2.69, 2.37, 3.54});

        //predict to a file
//        predict(champion, "data/predict.tsv", "out/prediction.tsv");

        //run the evolutionary algorithm to determine other possible weights
        runEvolution(champion);
    }

    static void predict(Predictor predictor, String predictionFilePath, String outFile){
        predictor.trainOnAll();
        try {
            PrintWriter writer = new PrintWriter(outFile, "UTF-8");
            writer.println("userID\tmovieID\trating");

            new Loader(predictionFilePath, new Function<String[], String>() {
                public String apply(String[] parts) {

                    long prediction = Math.round(predictor.predict(
                            User.findById(Integer.parseInt(parts[0])),
                            Movie.findById(Integer.parseInt(parts[1]))
                    ));

                    writer.println(parts[0] + "\t" + parts[1] + "\t" + prediction);

                    return "";
                }
            });
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //runs an evolutionary algorithm to vary the wieghts
    static void runEvolution(Predictor champion){
        int sampleSize = 50;
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
