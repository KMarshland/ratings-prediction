/**
 * Created by kaimarshland on 10/14/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.List;

public class User implements Distanceable {

    //keep track of all the users
    protected static List<User> users = new ArrayList<>();

    public enum Gender{
        Male,
        Female
    }

    //store info about each user
    private int id;
    private int age;
    private Gender gender;
    private List<Rating> ratings;
    private List<Movie> ratedMovies;
    private HashMap<Integer, Double> ratingsOf;

    public User(int id, int age, Gender gender){
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.ratings = new ArrayList<>();

        users.add(this);
    }

    //gives a list of all the movies that user has rated
    public List<Movie> ratedMovies(List<Rating> trainingSet){
        if (ratedMovies == null){
            ratedMovies = new ArrayList<>();
            for (Rating rating : ratings){
                if (!ratedMovies.contains(rating.getMovie())) {
                    ratedMovies.add(rating.getMovie());
                }
            }
        }

        //TODO: prevent this from being contaminated
        return ratedMovies;

//        List<Movie> rated = new ArrayList<>();
//        for (Movie movie : ratedMovies){
//            if (trainingSet.contains(rating)) {
//                rated.add(rating.getMovie());
//            }
//        }
//        return rated;
    }

    //figures out how the user rated that movie
    public double ratingOf(Movie movie, List<Rating> trainingSet){

        //TODO: prevent this from being contaminated
        if (ratingsOf == null){
            ratingsOf = new HashMap<>();
            for (Rating rating : ratings) {
                ratingsOf.put(rating.getMovieId(), (double)rating.getRating());
            }
        }

        if (ratingsOf.containsKey(movie.getId())){
            return ratingsOf.get(movie.getId());
        }

        return 0;
    }

    public double distanceTo(Distanceable other, double weight1, double weight2) {
        User otherUser = (User) other;

        return weight1 * (this.gender == otherUser.gender ? 1 : 0) +
                weight2 * Math.abs(this.getAge() - otherUser.getAge()) ;
    }

    public void addRating(Rating rating){
        ratings.add(rating);
    }

    public static User findById(int id){
        return users.get(id - 1);
    }

    public static void load(String source){
        new Loader(source, new Function<String[], String>() {
            public String apply(String[] parts) {
                new User(
                        Integer.parseInt(parts[0]), //id
                        Integer.parseInt(parts[1]), //age
                        (parts[2].equals("M")) ? Gender.Male : Gender.Female //gender
                );
                return "";
            }
        });
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        User.users = users;
    }

}
