import com.sun.tools.javac.code.Attribute;

import java.util.*;
import java.util.stream.DoubleStream;

/**
 * Created by kaimarshland on 10/15/15.
 */
public class Predictor {

    public enum TestMode{
        Accuracy,
        AverageError,
        RMSError
    }

    static TestMode testMode = TestMode.Accuracy;

    double[] weights;

    HashSet<Rating> trainingSet;

    public Predictor(double[] weights){
        this.weights = weights;
    }

    public String stringifyWeights(){
        String result = "";

        for (double d : weights){
            result += (Math.round(d * 100)/100.0) + ", ";
        }

        return result;
    }

    //creates children with the weights varied
    public List<Predictor> getChildren(){
        List<Predictor> children = new ArrayList<>();
        for (int i = 0; i < weights.length; i++){
            double[] weightsIncreased = Arrays.copyOf(weights, weights.length);
            double[] weightsDecreased = Arrays.copyOf(weights, weights.length);

            weightsIncreased[i] += Math.random();
            weightsDecreased[i] -= Math.random();

            children.add(new Predictor(weightsIncreased));
            children.add(new Predictor(weightsDecreased));
        }
        return children;
    }

    public void trainOnAll(){
        trainingSet = new HashSet<>();
        trainingSet.addAll(Rating.ratings);
    }

    //trains it on all but n ratings and returns the ratings it did not train on
    public List<Rating> train(int sampleSize){
        Collections.shuffle(Rating.ratings); //to randomize the order
        trainingSet = new HashSet<>();
        trainingSet.addAll(Rating.ratings.subList(sampleSize, Rating.ratings.size() - 1));
        return Rating.ratings.subList(0, sampleSize);
    }

    public double test(int sampleSize){
        return test(sampleSize, 2000/sampleSize);
    }

    //gives the average difference between predicted and actual ratings
    public double test(int sampleSize, int trials){
        //train it on the given sample size
        double total = 0;
        double totalAccuracy = 0;
        for (int i = 0; i < trials; i++){
            totalAccuracy += test(train(sampleSize), testMode);
        }
        return totalAccuracy/((double)trials);
    }

    //tests the given ratings. Assumes it's already been trained
    public double test(List<Rating> ratingsToTest, TestMode mode){
//        double total = 0;
        double result = 0;
        double sum = 0;

        //test all the predictions
        for (int i = 0; i < ratingsToTest.size(); i++){
            double expected = (double)ratingsToTest.get(i).getRating();
            double got = predict(ratingsToTest.get(i).getUser(), ratingsToTest.get(i).getMovie());

            if (!Double.isNaN(got)) {
                double difference = Math.abs(got - expected);
                sum++;

                if (mode == TestMode.Accuracy) {
                    if (difference < 0.5) {
                        result++;
                    }
                } else if (mode == TestMode.AverageError){
                    result += difference;
                } else if (mode == TestMode.RMSError){
                    result += difference*difference;
                }
            }
        }

        if (mode == TestMode.RMSError){
            return Math.sqrt(result/sum);
        } else {
            return result / sum;
        }
    }

    public double predict(User user, Movie movie){

        //find similar users
        double similarUserRating = 0;
        int totalCutoffUsers = 0;

        double similarUserSimilarMovie = 0;

        for (User compared : User.users){

            if (compared.getId() != user.getId()) {//don't compare to yourself

                double distance = user.distanceTo(compared, weights[4], weights[5]);

                if (distance < weights[6]) {
                    double rating = compared.ratingOf(movie, trainingSet);
                    if (!Double.isNaN(rating)) {
                        totalCutoffUsers++;
                        similarUserRating += rating;
                        similarUserSimilarMovie += compared.similarMovieRating(movie, trainingSet, weights[7], weights[8]);
                    }
                }
            }
        }
        similarUserRating /= (double)totalCutoffUsers;
        similarUserSimilarMovie /= (double)totalCutoffUsers;


        //figure out similar movies
        double similarMovieRating = user.similarMovieRating(movie, trainingSet, weights[7], weights[8]);


        //average rating of that movie
        double result = weights[0] * (movie.averageRating(trainingSet) + user.criticality(trainingSet));
        double dividedBy = weights[0];

        //how similar users rated that movie, if any did
        if (totalCutoffUsers > 0){
            result += weights[1] * similarUserRating;
            dividedBy += weights[1];

            //how similar users rated similar movies
            result += weights[2] * similarUserSimilarMovie;
            dividedBy += weights[2];
        }
        //how that user rated similar movies, if there were any
        if (!Double.isNaN(similarMovieRating)){
            result += weights[3] * similarMovieRating;
            dividedBy += weights[3];
        }

        return result/dividedBy; //normalize the rating
    }
}
