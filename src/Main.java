/**
 * Created by kaimarshland on 10/14/15.
 */
public class Main {

    public static void main(String[] args){
        loadAll();

//        System.out.println(Rating.getRatings().get(0).getUser().getId());
    }

    //loads everything and establishes relations
    static void loadAll(){
        User.load("users.tsv");
        Movie.load("movies.tsv");
        Rating.load("ratings.tsv");

        //build the has/belongs to relationships
        Rating.connectAll();
    }
}
