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


    /**
     * {@inheritDoc}
     */
    @Override
    protected String generateKey(@NotNull User user) {
        return user.getKey();
    }


    /**
     *  Retrives a user given his {@param email}
     * @param email email of the user
     * @return the retrived User or null if not found
     */
    public User getByEmail(@NotNull final String email) {
        User user = data.values().stream().filter(u -> email.equals(u.getEmail())).findAny().orElse(null);
        if (user == null ) return  null;

        User returnUser = user.makeCopy();
        returnUser.lockKey();
        return returnUser;
    }




    /**
     * {@inheritDoc}
     */
    public boolean update(@NotNull final String username,@NotNull final User updatedUser) {
        if (!data.containsKey(username)) {
            return false;
        }
        //The new username already exists
        if (!username.equals(updatedUser.getUsername()) && data.containsKey(updatedUser.getUsername())) {
            return false;
        }
        final User olduser = data.get(username);
        //email already used by another user
        if (!olduser.getEmail().equals(updatedUser.getEmail()) && getByEmail(updatedUser.getEmail()) != null) {
            return false;
        }

        //The old username could be different so we remove it
        data.remove(username);
        data.put(updatedUser.getUsername(), updatedUser.makeCopy());
        return true;
    }

}
