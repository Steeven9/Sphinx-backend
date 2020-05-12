package ch.usi.inf.sa4.sphinx.view;

/**
 * Represents the serialised version of a User entity
 * @see ch.usi.inf.sa4.sphinx.model.User
 */
public class SerialisableUser {
    public String username;
    public String email;
    public String fullname;
    public String password;
    public Integer[] rooms;
    public Boolean allowSecurityCameras;

    /** Constructor.**/
    public SerialisableUser(){

    }
}
