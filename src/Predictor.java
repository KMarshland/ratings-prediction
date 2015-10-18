import java.util.*;
import java.util.stream.DoubleStream;

/**
 * Created by kaimarshland on 10/15/15.
 */
public class Predictor {

    double[] weights;
    HashMap<Integer, Double> averageRatings; //the average ratings for the movies in the training set

    public Predictor(double[] weights){
        this.weights = weights;
    }

    //trains it on % of ratings and returns the ones it didn't train on
    public List<Rating> train(double samplePercentage){
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


        //calculate the average ratings for each movie
        averageRatings = new HashMap<>();
        HashMap<Integer, Integer> totalEntries = new HashMap<>();

        for (Rating r : ratings){
            if (averageRatings.containsKey(r.getMovieId())){
                averageRatings.put(r.getMovieId(), averageRatings.get(r.getMovieId()) + r.getRating());
                totalEntries.put(r.getMovieId(), totalEntries.get(r.getMovieId()) + 1);
            } else {
                averageRatings.put(r.getMovieId(), (double)r.getRating());
                totalEntries.put(r.getMovieId(), 1);
            }
        }

        for (int movieId : totalEntries.keySet()){//actually average it back down
            averageRatings.put(movieId, averageRatings.get(movieId) / ((double)totalEntries.get(movieId)) );
        }


        return this;//to allow chaining
    }

    //gives the average difference between predicted and actual ratings
    public double test (double samplePercentage){
        //train it on the given sample size
        return test(train(samplePercentage));
    }

    //gives the average difference between predicted and actual ratings
    public double test(int sampleSize){
        //train it on the given sample size
        return test(train(sampleSize));
    }

    //tests the given ratings. Assumes it's already been trained
    public double test(List<Rating> ratingsToTest){
        //test all the predictions
        double[] differences = new double[ratingsToTest.size()];
        for (int i = 0; i < ratingsToTest.size(); i++){
            int expected = ratingsToTest.get(i).getRating();
            double got = predict(ratingsToTest.get(i).getUser(), ratingsToTest.get(i).getMovie());

            //we want to minimize the absolute difference between
            differences[i] = Math.abs(got-expected);
        }

        //average the difference
        return (double) DoubleStream.of(differences).average().getAsDouble();
    }

    public double predict(User user, Movie movie){

        //find similar users
        double similarUserRating = 0;
        int totalCutoffUsers = 0;

        for (User compared : User.users){

            if (compared.getId() != user.getId()) {//don't compare to yourself

                double distance = user.distanceTo(compared, weights[3], weights[4]);

                if (distance < weights[5]) {
                    //TODO: make sure this rating is in the training set
                    double rating = compared.ratingOf(movie);
                    if (rating > 0) {
                        totalCutoffUsers++;
                        similarUserRating += rating;
                    }
                }
            }
        }

        //TODO: do something when totalCutoffUsers is 0
        similarUserRating /= (double)totalCutoffUsers;


        //figure out similar movies
        double averageMovieRating = 0;
        int totalCutoffMovies = 0;
        for (Movie compared : user.ratedMovies()){
            double distance = user.distanceTo(compared, weights[6], weights[7]);

            if (distance < weights[8]){
                totalCutoffMovies ++;
                //TODO: make sure the rating is in the training set
                averageMovieRating += user.ratingOf(compared);
            }
        }
        averageMovieRating /= (double)(totalCutoffMovies);


        return weights[0] * averageRatings.get(movie.getId()) + //average rating of that movie
                weights[1] * similarUserRating + //how similar users rated that movie
                weights[2] * averageMovieRating //how that user rated similar movies
                ;
    }
}
