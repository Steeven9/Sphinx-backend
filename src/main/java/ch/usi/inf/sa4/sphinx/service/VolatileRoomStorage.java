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
    private static final HashMap<Integer, Room> rooms = new HashMap<>();
    private static Integer id = 1;

    private Integer generateId(){
        return id++;
    }

    @Override
    public Room get(Integer roomId) {
        return rooms.get(roomId);
    }

    @Override
    public Integer insert(Room room) {
        Room savedRoom = room.makeCopy();
        savedRoom.setId(generateId());
        rooms.put(savedRoom.getId(), savedRoom);
        return savedRoom.getId();
    }

    @Override
    public boolean delete(Integer roomId) {
        rooms.remove(roomId);
        return this.get(roomId) == null;
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
