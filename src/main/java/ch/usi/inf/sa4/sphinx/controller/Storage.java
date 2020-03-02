package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;

import java.util.HashMap;

/**
 * Implements storage for the backend's data
 */
public class Storage {
    private static HashMap<String, User> users;

    /**
     * Retrieves a user given its username.
     * @param username the username of the requested user
     * @return the requested user or null if no user with that username exists.
     */
    public static User getUser(String username) {
        return users.get(username);
    }

    /**
     * Inserts a user.
     * @param user the user to insert
     */
    public static void insertUser(User user) {
        users.put(user.username, user);
    }

    /**
     * Deletes the user with the given username. Has no effect if no such user exists.
     * @param username the username of the user to delete
     */
    public static void deleteUser(String username) {
        users.remove(username);
    }

    /**
     * Retrieves a user given its email.
     * @param email the email of the requested user
     * @return the requested user or null if no user with that email exists
     */
    public static User getUserByEmail(String email) {
        return users.values().stream().findAny(user -> user.email == email).orElse(null);
    }
}
