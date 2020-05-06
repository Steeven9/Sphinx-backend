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
<<<<<<< HEAD
<<<<<<< HEAD
=======
    public boolean camVisible;
=======
    public Boolean guestsHaveCameraAccess;
>>>>>>> removed second @RequestBody in updateUser() and change camVisible variable to Boolean

    /** Constructor.**/
    public SerialisableUser(){

    }
>>>>>>> #124: allowed to update camVisibility in userController
}
