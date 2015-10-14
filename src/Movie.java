import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by kaimarshland on 10/14/15.
 */
public class Movie {

    //keep track of all the movies
    protected static ArrayList<Movie> movies = new ArrayList<>();

    //store info about each movie
    private int id;
    private String name;
    private String[] genres;

    public Movie(int id, String name, String[] genres){
        this.id = id;
        this.name = name;
        this.genres = genres;

        movies.add(this);
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
