package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;

public interface DeviceStorage {
    /**
     * Retrieves a Device given its id.
     *
     * @param deviceId the id of the requested room
     * @return the requested room or null if no room with that id exists.
     */
    Device get(final String deviceId);


    /**
     * Inserts a copy of the given Device into storage.
     *
     * @param device the room to insert
     * @return The id of the created device, null if it fails
     */
    String insert(final Device device);


    /**
     * Deletes the Device with the given Id. Has no effect if no such Device exists.
     *
     * @param deviceId the id of the Device to delete
     */
    void delete(String deviceId);


    /**
     * Updates the Room found with the id in updatedRoom with the fields of updatedRoom
     * @param updatedDevice the user with the updated field
     * @return true if the operation is successful else false
     */
    boolean updateDevice(final Device updatedDevice);
}
