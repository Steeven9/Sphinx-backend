package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/*
 * Volatile database for User
 *
 */



//METHOD ACCESS SHOULD BE DEFAULT NOT PUBLIC BUT IT CANT BE DONE SINCE IT IMPLEMENTS AN INTERFACE, MIGHT MAKE STORAGE
//AN ABSTRACT CLASS TO SET DIFFERENT PRIVACY.
@Repository("volatileUserStorage")
public class VolatileUserStorage implements UserStorage {
    //This is shared between all instances of the database
    private static final HashMap<String, User> users = new HashMap<>();

    @Override
    public User get(final String username) {
        return users.get(username);
    }

    @Override
    public User getByEmail(final String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);
    }

    @Override
    public String insert(final User user) {
        String username = user.getUsername();
        if (users.containsKey(username)) {
            return null;
        }

        //Notice that a copy constructor is used.
        users.put(username, user.makeCopy());
        return username;
    }

    @Override
    public void delete(String username) {
        users.remove(username);
    }

    @Override
    public boolean update(final String username, final User updatedUser) {
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

        //The old username could be different so we remove it
        users.remove(username);
        users.put(updatedUser.getUsername(), updatedUser);
        return true;
    }



}
