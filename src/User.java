/**
 * Created by kaimarshland on 10/14/15.
 */

import java.util.ArrayList;
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

    public User(int id, int age, Gender gender){
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.ratings = new ArrayList<>();

        users.add(this);
    }

    public float distanceTo(Distanceable other, float[] weights) throws Exception {
        User otherUser = (User) other;

        if (weights.length != 2){
            throw new IllegalArgumentException("Wrong number of weights. Expected 2, got: " + weights.length);
        }

        return weights[0] * (this.gender == otherUser.gender ? 1 : 0) +
                weights[1] * Math.abs(this.getAge() - otherUser.getAge()) ;
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
