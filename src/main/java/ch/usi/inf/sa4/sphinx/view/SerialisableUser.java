package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.User;

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

    public SerialisableUser(User user){
        this.username = user.username;
        this.email = user.email;
        this.fullname = user.fullname;
        this.password = user.password;
        this.rooms = new int[user.rooms.length];
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i] = user.rooms[i].id;
        }
    }
}
