package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;

import java.util.HashMap;

/**
 * Implements storage for the backend's data
 */
public class Storage {
    private static HashMap<String, User> users;
    private static HashMap<Integer, Room> rooms;
    private static HashMap<Integer, Device> devices;

    /**
     * Retrieves a user given its username.
     * @param username the username of the requested user
     * @return the requested user or null if no user with that username exists.
     */
    public static User getUser(String username) {
        return users.get(username);
    }

    /**
     * Retrieves a room given its id.
     * @param id the id of the requested room
     * @return the requested room or null if no room with that id exists.
     */
    public static Room getRoom(Integer id) {
        return rooms.get(id);
    }

    /**
     * Retrieves a device given its id.
     * @param id the id of the requested device
     * @return the requested device or null if no device with that id exists.
     */
    public static Device getDevice(Integer id) {
        return devices.get(id);
    }

    /**
     * Inserts a user.
     * @param user the user to insert
     */
    public static void insertUser(User user) {
        users.put(user.username, user);
    }

    /**
     * Inserts a room.
     * @param room the room to insert
     */
    public static void insertRoom(Room room) {
        rooms.put(room.id, roon);
    }

    /**
     * Inserts a device.
     * @param device the device to insert
     */
    public static void insertDevice(Device device) {
        devices.put(device.id, device);
    }

    /**
     * Deletes the user with the given username. Has no effect if no such user exists.
     * @param username the username of the user to delete
     */
    public static void deleteUser(String username) {
        users.remove(username);
    }

    /**
     * Deletes the room with the given id. Has no effect if no such room exists.
     * @param id the id of the room to delete
     */
    public static void deleteRoom(Integer id) {
        rooms.remove(id);
    }

    /**
     * Deletes the device with the given id. Has no effect if no such device exists.
     * @param id the id of the device to delete
     */
    public static void deleteDevice(Integer id) {
        devices.remove(id);
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
