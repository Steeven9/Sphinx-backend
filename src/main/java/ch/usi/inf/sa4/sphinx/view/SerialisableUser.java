package ch.usi.inf.sa4.sphinx.view;

import io.swagger.models.auth.In;

/**
 * Represents the serialised version of a User entity
 * @see ch.usi.inf.sa4.sphinx.model.User
 */
public class SerialisableUser {
    private String username;
    private String email;
    private String fullname;
    private String password;
    private Integer[] rooms;

    private Boolean allowSecurityCameras;


    /** Constructor.**/
    public SerialisableUser(){

    }

    /** Getter for the user username.
     * @return the user's username
     * **/
    public String getUsername(){
        return username;
    }

    /** Getter for the user email.
     * @return the user's email
     * **/
    public String getEmail(){
        return email;
    }

    /** Getter for the user full name.
     * @return the user's full name
     * **/
    public String getFullname(){
        return fullname;
    }

    /** Getter for the user password.
     * @return the user's password
     * **/
    public String getPassword(){
        return password;
    }

    /** Getter for the rooms of a user.
     * @return the user's rooms
     * **/
    public Integer[] getRooms(){
        return rooms;
    }

    /** Getter for the user security cameras access state.
     * @return the user's security cameras access state
     * **/
    public Boolean getAllowSecurityCameras(){
        return allowSecurityCameras;
    }

    /** Setter for the user's username.
     * @param  newUsername the user's new username
     * **/
    public void setUsername(String newUsername){
        username = newUsername;
    }

    /** Setter for the user's email.
     * @param  newEmail the user's new email
     * **/
    public void setEmail(String newEmail){
        email = newEmail;
    }

    /** Setter for the user's full name.
     * @param newFullName the user's new full name
     * **/
    public void setFullname(String newFullName){
        fullname = newFullName;
    }

    /** Setter for the user's password.
     * @param  newPassword the user's new password
     * **/
    public void setPassword(String newPassword){
        password = newPassword;
    }

    /** Setter for the user's security cameras access state.
     * @param  newState the user's new security cameras access state
     * **/
    public void setAllowSecurityCameras(Boolean newState){
        allowSecurityCameras = newState;
    }

    /** Setter for the ids of the user's rooms.
     * @param  newRoomIds the user's new security cameras access state
     * **/
    public void setRoomIds(Integer[] newRoomIds){
        rooms = newRoomIds;
    }

}
