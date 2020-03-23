package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;

public interface RoomStorage {
    /**
     * Retrieves a room given its id.
     *
     * @param roomId the id of the requested room
     * @return the requested room or null if no room with that id exists.
     */
    Room get(final String roomId);


    /**
     * Inserts a copy of the given room into storage.
     *
     * @param room the room to insert
     * @return a copy of the Room stored or null if the operation fails
     */
    String insert(final Room room);


    /**
     * Deletes the room with the given Id. Has no effect if no such room exists.
     *
     * @param roomId the id of the room to delete
     * @return
     */
     boolean delete(String roomId);


    /**
     * Updates the Room found with the id in updatedRoom with the fields of updatedRoom
     * @param updatedRoom the user with the updated field
     * @return true if the operation is successful else false
     */
    boolean update(final Room updatedRoom);
}
