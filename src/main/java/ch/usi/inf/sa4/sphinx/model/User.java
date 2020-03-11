package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

import java.util.List;

public class User {

    public User(String username, String password, String email, String fullname) {
        throw new NotImplementedException();
    }

    public String getPassword() {
        throw new NotImplementedException();
    }
    public String getUsername() {
        throw new NotImplementedException();
    }
    public boolean isVerified() {
        throw new NotImplementedException();
    }
    public String getVerificationToken() {
        throw new NotImplementedException();
    }
    public void verify() {
        throw new NotImplementedException();
    }
    public String getEmail() {
        throw new NotImplementedException();
    }
    public String getSessionToken() {
        throw new NotImplementedException();
    }
    public String getFullname() {
        throw new NotImplementedException();
    }
    public List<Room> getRooms() {
        throw new NotImplementedException();
    }
    public void setFullname(String newName) {
        throw new NotImplementedException();
    }
    public void setEmail(String newName) {
        throw new NotImplementedException();
    }
    public void setPassword(String newName) {
        throw new NotImplementedException();
    }

    public String createSessionToken(){
        throw new NotImplementedException();
    }
}