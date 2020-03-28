package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.view.SerialisableUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class User extends Storable<String>{
    private String email;
    private String password;
    private String username;
    private String fullname;
    private String resetCode;
    private final List<Integer> rooms;
    private String sessionToken;
    private final String verificationToken;
    private boolean verified;


    @Override
    public boolean setKey( String key) {
        return this.setUsername();
    }

    @Override
    public String getKey() {
        return this.getUsername();
    }

    /**
     * Constructor.
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
        this.verificationToken = UUID.randomUUID().toString();
        super.setKey(username);
    }

    /** Constructor.
     * @param  user a User
     **/
    private User(User user) {
        this.email = user.email;
        this.verificationToken = user.verificationToken;
        this.password = user.password;
        this.username = user.username;
        this.fullname = user.fullname;
        this.resetCode = user.resetCode;
        this.rooms = new ArrayList<>(user.rooms);
        this.sessionToken = user.sessionToken;
        this.verified = user.verified;
    }


    /**
     * Makes a copy of User.
     * @return a deep copy of this Object
     */
    public User makeCopy() {
        return new User(this);
    }

    /**
     * Getter for email.
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }


    /**
     * Getter for password.
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for username.
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }


    /**
     * Getter for fullname.
     * @return fullname of the user
     */
    public String getFullname() {
        return fullname;
    }


    /**
     * Getter for resetcode.
     * @return reset code of the user used to ...?
     */
    public String getResetCode() {
        return resetCode;
    }


    /**
     * Getter for rooms.
     * @return returns a list of the Ids of the rooms owned by the user
     */
    public List<Integer> getRooms() {
        return rooms;
    }


    /**
     * Getter for session token.
     * @return the session token of the user
     */
    public String getSessionToken() {
        return sessionToken;
    }


    /**
     * Setter for session token.
     * @param sessionToken  the session token
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * Getter for the verification status of the user.
     * @return true if the user is verified (he clicked the confirmation link sent by mail)
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Getter for verification Token.
     * @return confirmation code of the user, to be sent via mail and then confirmed by the user
     */
    public String getVerificationToken() {
        return verificationToken;
    }


    /**
     * Setter for email field.
     * @param email email of the user
     */
    public void setEmail(final String email) {
        this.email = email;

    }

    /**
     * Setter for password.
     * @param password new password of the user
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the username.
     * @param username username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Setter for full user name.
     * @param fullname full name of the user
     */
    public void setFullname(final String fullname) {
        this.fullname = fullname;
    }


    /**
     * Sets the verified status of the user to true.
     * @param status the status of the user
     */
    public void setVerified(final boolean status) {
        this.verified = status;
    }


    /**
     * Sets the status of the user to verified.
     */
    public void verify() {
        setVerified(true);

    }

    /**
     * Adds a the given roomId to the User.
     * @param roomId id of the room to be added
     */
    public void addRoom(final Integer roomId) {
        rooms.add(roomId);
    }

    /**
     * Removes the room with the selected id.
     * @param roomId id of the room to remove
     */
    public void removeRoom(final Integer roomId) {
        rooms.remove(roomId);//Needed otherwise it will just remove the index
    }


    /**
     * Generates and sets a session token for the  user.
     * @return the generated session token
     */
    public String createSessionToken() {
        sessionToken = UUID.randomUUID().toString();
        return sessionToken;
    }

    /**
     * Generates and sets a reset code for the user.
     * @return the generated reset code
     */
    public String createResetCode() {
        resetCode = UUID.randomUUID().toString();
        return resetCode;
    }

    /** Serializes a User.
     * @return a SerialisableUser
     **/
    public SerialisableUser serialise() {
        SerialisableUser sd = new SerialisableUser();
        sd.email = this.email;
        sd.fullname = this.fullname;
        sd.password = this.password;
        sd.rooms = this.rooms.toArray(new Integer[0]);
        return sd;
    }
}

