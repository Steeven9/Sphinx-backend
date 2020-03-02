package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;

import java.util.HashMap;

public class Storage {
    private static HashMap<String, User> users;

    public static User getUser(String username) {
        return users.get(username);
    }

    public static void insertUser(User user) {
        users.put(user.username, user);
    }

    public static void deleteUser(String username) {
        users.remove(username);
    }
}
