import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Movie implements Distanceable {

    //keep track of all the movies
    protected static ArrayList<Movie> movies = new ArrayList<>();

    //store info about each movie
    private int id;
    private String name;
    private String[] genres;
    private ArrayList<Rating> ratings;

    public Movie(int id, String name, String[] genres){
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.ratings = new ArrayList<>();

        movies.add(this);
    }

    public float distanceTo(Distanceable other, float[] weights) throws Exception {
        Movie otherMovie = (Movie) other;

        if (weights.length != 3){
            throw new Exception("Wrong number of weights. Expected 3, got: " + weights.length);
        }

        //find the longest substring in common between the two names (Eg "Mad Max" and "Mad Max: Fury Road" would give "Mad Max")

        //take the shorter of the two names
        String shorterName;
        String longerName;
        if (this.name.length() < otherMovie.name.length()){
            shorterName = this.name;
            longerName = otherMovie.name;
        } else {
            shorterName = otherMovie.name;
            longerName = this.name;
        }

        String longestSubstring = Main.longestSubstring(shorterName.toLowerCase(), longerName.toLowerCase());

        //find the number of genres that they have in common
        int genresMatches = 0;
        for (String genre : genres){ // O(n^2), which sucks, but at least n <= 4
            for (String otherGenre : otherMovie.genres){
                if (genre.equals(otherGenre)){
                    genresMatches ++;
                }
            }
        }

        return weights[0] * longestSubstring.length() +
                weights[1] * genresMatches;
    }

    public void addRating(Rating rating){
        ratings.add(rating);
    }

    public static Movie findById(int id){
        return movies.get(id - 1);
    }

    public static void load(String source){
        new Loader(source, new Function<String[], String>() {
            public String apply(String[] parts) {
                new Movie(
                        Integer.parseInt(parts[0]), //id
                        parts[1], //name
                        Arrays.copyOfRange(parts, 2, parts.length) //genres
                );
                return "";
            }
        });
    }

    public static ArrayList<Movie> getMovies() {
        return movies;
    }

    public static void setMovies(ArrayList<Movie> movies) {
        Movie.movies = movies;
    }
}
