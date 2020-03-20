package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Room;

public class VolatileRoomStorage implements RoomStorage{


    Room get(final String roomId);



    Room insert(final Room room);


    public void delete(String roomId);



    boolean update(final Room updatedRoom);
}
