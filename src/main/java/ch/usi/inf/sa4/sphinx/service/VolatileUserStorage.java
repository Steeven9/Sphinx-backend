package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/*
 * Volatile database for User
 *
 */


@Repository("volatileUserStorage")
public class VolatileUserStorage extends VolatileStorage<String, User> {
    //This is shared between all instances of the database
    private static final HashMap<String, User> users = new HashMap<>();

    @Override
    protected String generateKey(@NotNull User user) {
        return user.getKey();
    }

    public User getByEmail(@NotNull final String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);
    }



    public boolean update(@NotNull final String username,@NotNull final User updatedUser) {
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
