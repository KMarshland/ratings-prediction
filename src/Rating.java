import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Rating {
    //keep track of all the ratings
    protected static List<Rating> ratings = new ArrayList<>();

    //store info about each rating
    private int userId;
    private int movieId;
    private int rating;

    private User user;
    private Movie movie;

    public Rating(int userId, int movieId, int rating){
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;

        ratings.add(this);
    }

    void connect(){
        if (user == null){
            user = User.findById(userId);
            user.addRating(this);
        }

        if (movie == null){
            movie = Movie.findById(movieId);
            movie.addRating(this);
        }
    }

    //establishes connections for all the ratings to their users and movies
    public static void connectAll(){
        for (Rating r : ratings){
            r.connect();
        }
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

    public static List<Rating> getRatings() {
        return ratings;
    }

    public static void setRatings(List<Rating> ratings) {
        Rating.ratings = ratings;
    }

    public Movie getMovie() {
        return movie;
    }

    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getUserId() {
        return userId;
    }
}
