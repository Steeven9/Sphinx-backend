package ch.usi.inf.sa4.sphinx.view;

public class SerialisableUser {
    public String username;
    public String email;
    public String fullname;
    public String password;
    public Integer[] rooms;


    /** Constructor.**/
    public SerialisableUser(){

    }

    /** Constructor.
     * @param email the email of the user
     * @param fullname the full name of the user
     * @param password the user's password
     * @param rooms the list of the user's room(s)  (by id)
     * @param username the user's username
     **/
    public SerialisableUser(String username, String email, String fullname, String password, Integer[] rooms){
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.rooms = rooms;
    }
}
