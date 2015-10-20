import java.util.*;
import java.util.stream.DoubleStream;

/**
 * Created by kaimarshland on 10/15/15.
 */
public class Predictor {

    double[] weights;

    HashSet<Rating> trainingSet;

    public Predictor(double[] weights){
        this.weights = weights;
    }

    public String stringifyWeights(){
        return Arrays.toString(weights);
    }

    //creates children with the weights varied
    public List<Predictor> getChildren(){
        List<Predictor> children = new ArrayList<>();
        for (int i =0; i < weights.length; i++){
            double[] weightsIncreased = Arrays.copyOf(weights, weights.length);
            double[] weightsDecreased = Arrays.copyOf(weights, weights.length);

            weightsIncreased[i] += Math.random();
            weightsDecreased[i] -= Math.random();

            children.add(new Predictor(weightsIncreased));
            children.add(new Predictor(weightsDecreased));
        }
        return children;
    }

    //trains it on % of ratings and returns the ones it didn't train on
//    public List<Rating> train(double samplePercentage){
//        if (samplePercentage < 0f || samplePercentage > 1f){
//            throw new IllegalArgumentException("Sample size must be between 0 and 1");
//        }
//        return train((int)(samplePercentage * Rating.getRatings().size()));
//    }

    //trains it on all but n ratings and returns the ratings it did not train on
    public List<Rating> train(int sampleSize){
        Collections.shuffle(Rating.ratings); //to randomize the order
        trainingSet = new HashSet<>();
        trainingSet.addAll(Rating.ratings.subList(sampleSize, Rating.ratings.size() - 1));
        return Rating.ratings.subList(0, sampleSize);
    }

    //gives the average difference between predicted and actual ratings
//    public double test (double samplePercentage){
//        //train it on the given sample size
//        return test(train(samplePercentage));
//    }

    //gives the average difference between predicted and actual ratings
    public double test(int sampleSize){
        //train it on the given sample size
        return test(train(sampleSize));
    }

    //tests the given ratings. Assumes it's already been trained
    public double test(List<Rating> ratingsToTest){
        double total = 0;
        double sum = 0;

        //test all the predictions
        double[] differences = new double[ratingsToTest.size()];
        for (int i = 0; i < ratingsToTest.size(); i++){
            double expected = (double)ratingsToTest.get(i).getRating();
            double got = predict(ratingsToTest.get(i).getUser(), ratingsToTest.get(i).getMovie());

            if (!Double.isNaN(got)) {
                //we want to minimize the absolute difference between
                total += Math.abs(got - expected);
                sum++;

//                if (got == expected){
//                    System.err.println("Got is: " + got);
//                    throw new RuntimeException("Fuck you too");
//                }
            }
        }

        double result = total/sum;
//        System.out.println("Test result: " + result);

        return result;
    }

    public double predict(User user, Movie movie){

        //find similar users
        double similarUserRating = 0;
        int totalCutoffUsers = 0;

        for (User compared : User.users){

            if (compared.getId() != user.getId()) {//don't compare to yourself

                double distance = user.distanceTo(compared, weights[3], weights[4]);

                if (distance < weights[5]) {
                    double rating = compared.ratingOf(movie, trainingSet);
                    if (!Double.isNaN(rating)) {
                        totalCutoffUsers++;
                        similarUserRating += rating;
                    }
                }
            }
        }
        similarUserRating /= (double)totalCutoffUsers;


        //figure out similar movies
        double similarMovieRating = 0;
        int totalCutoffMovies = 0;
        for (Movie compared : user.ratedMovies(trainingSet)){
            double distance = movie.distanceTo(compared, weights[6], weights[7]);

            if (distance < weights[8]){
                totalCutoffMovies ++;
                similarMovieRating += user.ratingOf(compared, trainingSet);
            }
        }
        similarMovieRating /= (double)(totalCutoffMovies);


        //average rating of that movie
        double result = weights[0] * movie.averageRating(trainingSet);
        double dividedBy = weights[0];

        //how similar users rated that movie, if any did
        if (totalCutoffUsers > 0){
            result += weights[1] * similarUserRating;
            dividedBy += weights[1];
        }
        //how that user rated similar movies, if there were any
        if (totalCutoffMovies > 0){
            result += weights[2] * similarMovieRating;
            dividedBy += weights[2];
        }

        return result/dividedBy; //normalize the rating
    }
}
