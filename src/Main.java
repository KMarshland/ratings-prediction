/**
 * Created by kaimarshland on 10/14/15.
 */
public class Main {

    public static void main(String[] args){
        User.load("users.tsv");
        Movie.load("movies.tsv");
        Rating.load("ratings.tsv");

        System.out.println(Rating.getRatings().size());
    }
}
