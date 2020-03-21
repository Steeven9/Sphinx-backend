package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;

public interface UserStorage {

    /**
     * Retrieves a user given its username.
     *
     * @param username the username of the requested user
     * @return the requested user or null if no user with that username exists.
     */
    User get(final String username);


    /**
     * Retrieves a user given its email.
     *
     * @param email the email of the requested user
     * @return the requested user or null if no user with that email exists
     */
    User getByEmail(final String email);


    /**
     * Inserts a user.
     *
     * @param user the user to insert
     * @return the key for User (username) if success or null if fails
     */
    //It now returns the User for symmetry with the other Storage that possibly return a copy with a generated unique ID
    String insert(final User user);


    /**
     * Deletes the user with the given username. Has no effect if no such user exists.
     *
     * @param username the username of the user to delete
     */
    void delete(String username);


    /**
     * Updates the User with the given username based on the fields of updatedUser, if the given username does not exist
     * returns false, if the required new username is already in use by another user returns false and likewise for
     * the email field.
     * If the updated username is different from the old one then the old entry is removed and the new one inserted.
     * Notice that a copy of the updatedUser is stored.
     *
     * @param username    the username of the user to update
     * @param updatedUser the user with the updated field
     * @return true if the operation is successful else false
     */
    boolean updateUser(final String username, final User updatedUser);


}
