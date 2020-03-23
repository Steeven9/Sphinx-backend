package ch.usi.inf.sa4.sphinx.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




public class User {
    private String email;
    private String password;
    private String username;
    private String fullname;
    private final String confirmCode;
    private String resetCode;
    private final List<String> rooms;
    private String sessionToken;
    private final String verificationToken;
    private Boolean verified;


    /**
     * @param email    user email: can't be the same as other users
     * @param password user password
     * @param username username: can't be the same as other users
     * @param fullname full name
     */
    public User(final String email, final String password, final String username, final String fullname) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.fullname = fullname;
        this.rooms = new ArrayList<>();
        this.verified = false;
        this.confirmCode = UUID.randomUUID().toString();
        this.verificationToken = UUID.randomUUID().toString();
    }


    private User(User user) {
        this.email = user.email;
        this.verificationToken = user.verificationToken;
        this.password = user.password;
        this.username = user.username;
        this.fullname = user.fullname;
        this.confirmCode = user.confirmCode;
        this.resetCode = user.resetCode;
        this.rooms = new ArrayList<String>(user.rooms);
        this.sessionToken = user.sessionToken;
        this.verified = user.verified;
    }


    /**
     * @return a deep copy of this Object
     */
    public User makeCopy(){
        return new User(this);
    }

    /**
     * getter for email
     *
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }


    /**
     * getter for password
     *
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter for username
     *
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }


    /**
     * getter for fullname
     *
     * @return fullname of the user
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * getter for confirmation code
     *
     * @return confirmation code of the user, to be sent via mail and then confirmed by the user
     */
    public String getConfirmCode() {
        return confirmCode;
    }


    /**
     * getter for resetcode
     *
     * @return reset code of the user used to ...?
     */
    public String getResetCode() {
        return resetCode;
    }


    /**
     * getter for rooms
     *
     * @return returns a list of the Ids of the rooms owned by the user
     */
    public List<String> getRooms() {
        return rooms;
    }


    /**
     * getter for session token
     *
     * @return the session token of the user
     */
    public String getSessionToken() {
        return sessionToken;
    }


    /**
     * getter for the verification status of the user
     *
     * @return true if the user is verified (he clicked the confirmation link sent by mail)
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * getter for verification Token
     *
     * @return confirmation code of the user, to be sent via mail and then confirmed by the user
     */
    public String getVerificationToken() {
        return verificationToken;
    }

    /**
     * setter for email field
     *
     * @param email email of the user
     */
    public void setEmail(final String email) {
        this.email = email;

    }

    /**
     * setter for password
     *
     * @param password new password of the user
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * sets the username
     *
     * @param username username
     */
    public void setUsername(final String username) {
        this.username = username;
    }


    /**
     * setter for full user name
     *
     * @param fullname full name of the user
     */
    public void setFullname(final String fullname) {
        this.fullname = fullname;
    }


    /**
     * sets the verified status of the user to true
     */
    public void setVerified(final boolean status) {
        this.verified = status;
    }


    /**
     * adds a the given roomId to the User
     *
     * @param roomId id of the room to be added
     */
    public void addRoom(final String roomId) {
        rooms.add(roomId);
    }


    /**
     * removes the room with the selected id
     *
     * @param roomId id of the room to remove
     */
    public void removeRoom(final String roomId) {
        rooms.remove(roomId);//Needed otherwise it will just remove the index
    }


    /**
     * generates and sets a session token for the  user
     *
     * @return the generated session token
     */
    public String createSessionToken() {
        sessionToken = UUID.randomUUID().toString();
        return sessionToken;
    }


    /**
     * generates and sets a reset code for the user
     *
     * @return the generated reset code
     */
    public String createResetCode() {
        resetCode = UUID.randomUUID().toString();
        return resetCode;
    }
}



