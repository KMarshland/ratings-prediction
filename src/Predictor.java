import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by kaimarshland on 10/15/15.
 */
public class Predictor {

    float[] weights;

    public Predictor(float[] weights){
        this.weights = weights;
    }

    //trains it on % of ratings and returns the ones it didn't train on
    public List<Rating> train(float samplePercentage){
        if (samplePercentage < 0f || samplePercentage > 1f){
            throw new IllegalArgumentException("Sample size must be between 0 and 1");
        }
        return train((int)(samplePercentage * Rating.getRatings().size()));
    }

    //trains it on all but n ratings and returns the ratings it did not train on
    public List<Rating> train(int sampleSize){
        Collections.shuffle(Rating.ratings); //to randomize the order
        train(Rating.ratings.subList(0, Rating.ratings.size() - 1 - sampleSize));
        return Rating.ratings.subList(0, sampleSize);
    }

    //trains the predictor on the given set of data
    public Predictor train(List<Rating> ratings){
        //TODO: actually make it train on the data

        return this;//to allow chaining
    }

    //gives the average difference between predicted and actual ratings
    public float test (float samplePercentage){
        //train it on the given sample size
        return test(train(samplePercentage));
    }

    //gives the average difference between predicted and actual ratings
    public float test(int sampleSize){
        //train it on the given sample size
        return test(train(sampleSize));
    }

    //tests the given ratings. Assumes it's already been trained
    public float test(List<Rating> ratingsToTest){
        //test all the predictions
        int[] differences = new int[ratingsToTest.size()];
        for (int i = 0; i < ratingsToTest.size(); i++){
            int expected = ratingsToTest.get(i).getRating();
            int got = predict(ratingsToTest.get(i).getUser(), ratingsToTest.get(i).getMovie());

            //we want to minimize the absolute difference between
            differences[i] = Math.abs(got-expected);
        }

        //average the difference
        return (float) IntStream.of(differences).average().getAsDouble();
    }

    public int predict(User user, Movie movie){
        //TODO: make it actually predict a rating, based on the weights

        return 0;
    }
}
