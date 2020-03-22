package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;


//METHOD ACCESS SHOULD BE DEFAULT NOT PUBLIC BUT IT CANT BE DONE SINCE IT IMPLEMENTS AN INTERFACE, MIGHT MAKE STORAGE
//AN ABSTRACT CLASS TO SET DIFFERENT PRIVACY.

//Docs in RoomStorage
@Component("volatileRoomStorage")
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
        rooms.put(savedRoom.getId(), savedRoom);
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
        return true;
    }
}
