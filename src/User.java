/**
 * Created by kaimarshland on 10/14/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
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
    private HashMap<Movie, Rating> ratedMovies;
    private HashMap<Integer, Rating> ratingsOf;

    private double criticality;

    public User(int id, int age, Gender gender){
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.ratings = new ArrayList<>();

        users.add(this);

        criticality = Double.NaN;
    }

    //whether this user is more or less critical than average
    public double criticality(HashSet<Rating> trainingSet){
        if (Double.isNaN(criticality)) {
            double total = 0;
            double sum = 0;
            for (Rating rating : ratings) {
                if (trainingSet.contains(rating)) {
                    total += rating.getRating() - rating.getMovie().averageRating(trainingSet);
                    sum++;
                }
            }
            criticality = total / sum;
        }
        return criticality;
    }

    //gives a list of all the movies that user has rated
    public List<Movie> ratedMovies(HashSet<Rating> trainingSet){
        if (ratedMovies == null){
            ratedMovies = new HashMap<>();
            for (Rating rating : ratings){
                Movie movie = rating.getMovie();
                if (!ratedMovies.containsKey(movie)) {
                    ratedMovies.put(movie, rating);
                }
            }
        }

        List<Movie> rated = new ArrayList<>();
        for (Map.Entry<Movie, Rating> entry : ratedMovies.entrySet()) {
            Movie movie = entry.getKey();
            Rating value = entry.getValue();
            if (trainingSet.contains(value)){
                rated.add(movie);
            }
        }

        return rated;
    }

    //figures out how the user rated that movie
    public double ratingOf(Movie movie, HashSet<Rating> trainingSet){

        if (ratingsOf == null){
            ratingsOf = new HashMap<>();
            for (Rating rating : ratings) {
                ratingsOf.put(rating.getMovieId(), rating);
            }
        }

        Rating r = ratingsOf.get(movie.getId());
        if (r != null && trainingSet.contains(r)) {
            return r.getRating();
        }

        return Double.NaN;
    }

    public double distanceTo(Distanceable other, double genderWeight, double ageWeight) {
        User otherUser = (User) other;

        return genderWeight * (this.gender == otherUser.gender ? 1 : 0) +
                ageWeight * Math.abs(this.getAge() - otherUser.getAge()) ;
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
