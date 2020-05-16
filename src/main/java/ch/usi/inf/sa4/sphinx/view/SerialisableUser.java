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
<<<<<<< HEAD
<<<<<<< HEAD
=======
    public boolean camVisible;
=======
    public Boolean guestsHaveCameraAccess;
>>>>>>> removed second @RequestBody in updateUser() and change camVisible variable to Boolean
=======
    public Boolean allowSecurityCameras;
>>>>>>> #124: change SerialisableUser field name for camera access
=======
    public Boolean allowSecurityCameras;
>>>>>>> 347d2e84e817cea701da328d3531144c574bab19

    /** Constructor.**/
    public SerialisableUser(){

    }
>>>>>>> #124: allowed to update camVisibility in userController
}
