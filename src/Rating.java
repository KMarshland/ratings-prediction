import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Rating {
    //keep track of all the ratings
    protected static ArrayList<Rating> ratings = new ArrayList<>();

    //store info about each rating
    private int userId;
    private int movieId;
    private int rating;

    public Rating(int userId, int movieId, int rating){
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;

        ratings.add(this);
    }

    public static void load(String source){
        new Loader(source, new Function<String[], String>() {
            public String apply(String[] parts) {
                new Rating(
                        Integer.parseInt(parts[0]), //user id
                        Integer.parseInt(parts[1]), //movie id
                        Integer.parseInt(parts[2]) //rating
                );
                return "";
            }
        });
    }

    public static ArrayList<Rating> getRatings() {
        return ratings;
    }

    public static void setRatings(ArrayList<Rating> ratings) {
        Rating.ratings = ratings;
    }
}
