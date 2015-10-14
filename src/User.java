/**
 * Created by kaimarshland on 10/14/15.
 */

import java.util.function.Function;
import java.util.ArrayList;

public class User {

    protected static ArrayList<User> users = new ArrayList<>();

    public enum Gender{
        Male,
        Female
    }

    int id;
    int age;
    Gender gender;

    public User(int id, int age, Gender gender){
        this.id = id;
        this.age = age;
        this.gender = gender;

        users.add(this);
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

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

}
