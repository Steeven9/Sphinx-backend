package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import com.google.gson.annotations.Expose;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *
 */
@Entity
@Table(name = "sh_user")
public class User extends StorableE {
    @Expose
    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String username;
    @Expose
    @Column(nullable = false, unique = true)
    @NotBlank
    private String email;
    @Expose(serialize = false)
    @NotBlank
    private String password;
    @Expose
    @NotBlank
    private String fullname;
    @Column(name = "reset_code")
    private String resetCode;
    @Expose(deserialize = false)
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private List<Room> rooms;
    @Column(name = "session_token")
    private String sessionToken;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<User> hosts;
    private boolean camsVisible;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private List<Scene> scenes;

    // @GeneratedValue(generator = "uuidGenerator")
    //  @GenericGenerator(name="uuidGenerator", strategy="ch.usi.inf.sa4.sphinx.service.User.uuidGenerator")
    @Column(name = "verification_token")
    private String verificationToken;
    @Expose(deserialize = false)
    private boolean verified;


    /**
     * @param email    user email: can't be the same as other users
     * @param password user password
     * @param username username: can't be the same as other users
     * @param fullname full name
     */
    public User(final String email, final String password, final String username, final String fullname) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(password);
        this.fullname = fullname;
        this.rooms = new ArrayList<>();
        this.verified = false;
        this.camsVisible = false;
        this.hosts = new ArrayList<>();
        this.scenes = new ArrayList<>();
        this.verificationToken = UUID.randomUUID().toString();
    }



    public User() {
        // default constructor required by JPA
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
     * getter for resetcode
     *
     *  * @return reset code of the user
     */
    public String getResetCode() {
        return resetCode;
    }
    /**
     * gets the rooms of this User
     * @return a list of rooms that belong to this user
     */
    public List<Room> getRooms() {
        return rooms;
    }

    public List<Scene> getScenes() {
        return scenes;
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
     * Sets this User's session token
     * @param sessionToken the new value of the session token
     */

    public void setSessionToken(final String sessionToken) {
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
     * setter for password, the password will be hashed
     *
     * @param password new password of the user
     */
    public void setPassword(final String password) {
        this.password = hashPassword(password);
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
     *
     * @param status the new status to set
     */
    public void setVerified(final boolean status) {
        this.verified = status;
    }


    /**
     * adds a the given room to the User notice that this won't update the storage version
     *
     * @param room the room to be added
     */
    public void addRoom(@NonNull final Room room){

        room.setUser(this); //looks weird but otherwise the foreign key in Room is not saved
        rooms.add(room);
    }


    /**
     * removes the room with the selected id
     *
     * @param roomId id of the room to remove
     */
    public void removeRoom(final Integer roomId) {
        rooms.removeIf(r -> r.getId().equals(roomId));
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


    /**
     * Serialises a User. Fields whose value cannot be determined by looking at the User are set to null.
     * @return a serialised version of the USer
     * @see SerialisableUser
     */
    public SerialisableUser serialise() {
        final SerialisableUser sd = new SerialisableUser();
        sd.setUsername(this.username);
        sd.setEmail(this.email);
        sd.setFullname(this.fullname);
        sd.setRoomIds(this.rooms.stream().map(Room::getId).toArray(Integer[]::new));
        sd.setAllowSecurityCameras(this.camsVisible);
        return sd;
    }


    /**
     * asserts if there's a match between the User's hashed password and the one in plaintext
     * @param password the plaintext password to check
     * @return true if matching else false
     */
    public boolean matchesPassword(@NonNull final String password){
        return BCrypt.checkpw(password, this.password);
    }


    /**
     * Given a password string, returns its salted and hashed equivalent.
     * @param password the string to hash
     * @return a string containing the hash of {@code password} and the salt used to hash it
     */
    private static String hashPassword(final String password){
        if (password == null) return null;
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * getter for guest
     *
     * @return returns a list of the houses the user has access to as guest
     */
    public List<User> getHosts() {
        return hosts;
    }

    /**
     * Add user to the list of user hub's our user has access to as guest.
     *
     * @param user the user to add
     **/
    public void addHost(final User user){
        hosts.add(user);
    }

    /**
     * Removes a house access from deleting a user's name from our list.
     *
     * @param user the user to remove
     **/
    public void removeHost(final User user){
        hosts.remove(user);
    }

    /**
     * Check if cameras are accessible by guests.
     *
     * @return true if the cameras are visible to the guests
     **/
    public Boolean areCamsVisible() {
        return camsVisible;
    }

    /**
     * Sets the cam visibility to the desired value.
     *
     * @param status the new value
     * */
    public void switchCamerasAccessibility(final boolean status){
        camsVisible = status;
    }

    /**
     * Serialiases a user as host but with only data about the username, email and full name.
     * @return a serialised version of the User
     * @see SerialisableUser
     */
    public SerialisableUser serialiseAsHost() {
        final SerialisableUser sd = new SerialisableUser();
        sd.setUsername(this.username);
        sd.setEmail(this.email);
        sd.setFullname(this.fullname);
        return sd;
    }
}

