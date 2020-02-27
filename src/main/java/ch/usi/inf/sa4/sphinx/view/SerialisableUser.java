package ch.usi.inf.sa4.sphinx.view;

public class SerialisableUser {
    public String username;
    public String email;
    public String fullname;
    public String password;
    public int[] rooms;

    public SerialisableUser(String username, String email, String fullname, String password, int[] rooms){
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.rooms = rooms;
    }
}
