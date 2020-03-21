package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Room;

import java.util.HashMap;
import java.util.UUID;

//Docs in RoomStorage
public class VolatileRoomStorage implements RoomStorage{
    private static final HashMap<String, Room> rooms = new HashMap<>();

    @Override
    public Room get(String roomId) {
       return rooms.get(roomId);
    }

    @Override
    public String insert(Room room) {
        Room savedRoom = room.makeCopy();
        savedRoom.setId(UUID.randomUUID().toString());
        rooms.put(savedRoom);
        return savedRoom.getId();
    }

    @Override
    public void delete(String roomId) {
        rooms.remove(roomId);
    }

    @Override
    public boolean update(Room updatedRoom) {
        if(!rooms.containsKey(updatedRoom.getId())){
            return false;
        }
        rooms.put(updatedRoom.getId(), updatedRoom.makeCopy());
    }
}
