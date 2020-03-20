package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;

import java.util.HashMap;

/**
 *
 */
public class UserStorage {
    private static final HashMap<String, User> users = new HashMap<>();

    private UserStorage() {
    }


    /**
     * Retrieves a user given its username.
     *
     * @param username the username of the requested user
     * @return the requested user or null if no user with that username exists.
     */
    public static User get(final String username) {
        return users.get(username);
    }


    /**
     * Retrieves a user given its email.
     *
     * @param email the email of the requested user
     * @return the requested user or null if no user with that email exists
     */
    public static User getByEmail(final String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);
    }


    /**
     * Inserts a user.
     *
     * @param user the user to insert
     * @return true if the insertion was successful, false otherwise
     */
    public static boolean insert(final User user) {
        if (users.containsKey(user.getUsername())) {
            return false;
        }

        users.put(user.getUsername(), new User(user));
        return true;
    }



    public static void delete(String username) {
        users.remove(username);
    }


    /**
     * Updates the User with the given username based on the fields of updatedUser, if the given username does not exist
     * returns false, if the required new username is already in use by another user returns false and likewise for
     * the email field.
     * If the updated username is different from the old one then the old entry is removed and the new one inserted.
     * Notice that a copy of the updatedUser is stored.
     * @param username the username of the user to update
     * @param updatedUser the user with the updated field
     * @return
     */
    public static boolean updateUser(final String username, final User updatedUser) {
        if (!users.containsKey(username)) {
            return false;
        }

        //The new username already exists
        if (!username.equals(updatedUser.getUsername()) && users.containsKey(updatedUser.getUsername())) {
            return false;
        }

        final User olduser = users.get(username);

        //email already used by another user
        if (!olduser.getEmail().equals(updatedUser.getEmail()) && getByEmail(updatedUser.getEmail()) != null) {
            return false;
        }

        //The old username could be differend so we remove it
        users.remove(username);
        users.put(updatedUser.getUsername(), updatedUser);
        return true;
    }


    public static boolean setUsername(String username, String updatedUsername){
        return users.containsKey(username) && !users.containsKey(updatedUsername);
    }


}
