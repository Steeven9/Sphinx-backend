package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.view.SerialisableUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *
 */
public class User extends Storable<String, User> {
    private String email;
    private String password;
    private String fullname;
    private String resetCode;
    private final List<Integer> rooms;
    private String sessionToken;
    private final String verificationToken;
    private boolean verified;


    /**
     * @param email    user email: can't be the same as other users
     * @param password user password
     * @param username username: can't be the same as other users
     * @param fullname full name
     */
    public User(final String email, final String password, final String username, final String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.rooms = new ArrayList<>();
        this.verified = false;
        this.verificationToken = UUID.randomUUID().toString();
        super.setKey(username);
    }

    private User(User user) {
        super.setKey(user.getKey());
        this.email = user.email;
        this.verificationToken = user.verificationToken;
        this.password = user.password;
        this.fullname = user.fullname;
        this.resetCode = user.resetCode;
        this.rooms = new ArrayList<>(user.rooms);
        this.sessionToken = user.sessionToken;
        this.verified = user.verified;
    }




    /**
     * @return a deep copy of this Object
     */
    public User makeCopy() {
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
        return getKey();
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
    public List<Integer> getRooms() {
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


    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * getter for the verification status of the user
     *
     * @return true if the user is verified (he clicked the confirmation link sent by mail)
     */
    public boolean isVerified() {
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
    public boolean setUsername(final String username) {
        return setKey(username);
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
     * Sets the status of the user to verified
     */
    public void verify() {
        setVerified(true);

    }

    /**
     * adds a the given roomId to the User
     *
     * @param roomId id of the room to be added
     */
    public void addRoom(final Integer roomId) {
        rooms.add(roomId);
    }

    /**
     * removes the room with the selected id
     *
     * @param roomId id of the room to remove
     */
    public void removeRoom(final Integer roomId) {
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


    public SerialisableUser serialise() {
        SerialisableUser sd = new SerialisableUser();
        sd.email = this.email;
        sd.fullname = this.fullname;
        sd.password = this.password;
        sd.rooms = this.rooms.toArray(new Integer[0]);
        return sd;
    }
}

