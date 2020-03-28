package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;
import ch.usi.inf.sa4.sphinx.model.User;

public class UserKeyGen extends KeyGen<String, User>{
    @Override
    public String generateKey(User user) {
        return user.getUsername();
    }
}
