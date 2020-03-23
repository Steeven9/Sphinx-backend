package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import  ch.usi.inf.sa4.sphinx.service.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;

    /**
     * @param sessionToken session token of the user
     * @param username the username of the user
     * @param errors in case error occur
     * @return a ResponseEntity with the array of rooms owned by the user
     */
    @GetMapping("/")
    public ResponseEntity<Room[]> getAllRooms(@NotNull @RequestHeader("session-token") String sessionToken,
                                                @NotNull @RequestHeader("user") String username,
                                                @RequestParam(name="filter", required=false)
                                                Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.status(401).build();
        }

        User user = Storage.getUser(username);
        if (user != null) {
            if (!user.getSessionToken().equals(sessionToken)) {
                return ResponseEntity.status(401).build();
            }

            Room[] arr = new Room[userService.getRooms(user).size()];
            return ResponseEntity.ok(userService.getRooms(user).toArray(arr));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Returns a room with all details about it.
     * @param roomId the id of the room
     * @param sessionToken session token of the user
     * @param username the username of the user
     * @return the room with all details about it
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<SerialisableRoom> getRoom(@PathVariable String roomId,
                                                      @RequestHeader("session-token") String sessionToken,
                                                      @RequestHeader("user") String username,
                                                        Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.status(401).build();
        }
        User user = Storage.getUser(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getSessionToken().equals(sessionToken)) {
            return ResponseEntity.status(401).build();
        }

        Room room = roomService.get(roomId);

        if (room != null) {
            return ResponseEntity.ok(new SerialisableRoom(room));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Given the room, returns all the devices in this room.
     * @param sessionToken session token of the user
     * @param username the username of the user
     * @param errors in case error occur
     * @return an array of devices in given room
     */
    @GetMapping("/{roomId}/devices/")
    public ResponseEntity<Device[]> getDevice(@PathVariable String roomId,
                                              @RequestHeader("session-token") String sessionToken,
                                              @RequestHeader("user") String username,
                                              Errors errors
                                                        ) {
        if (errors.hasErrors()){
            return ResponseEntity.status(401).build();
        }
        User user = Storage.getUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getSessionToken().equals(sessionToken)) {
            return ResponseEntity.status(401).build();
        }

        Room room = roomService.get(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        List<Device> list = roomService.getDevices(roomId);
        Device[] arr = new Device[list.size()];
        return ResponseEntity.ok(list.toArray(arr));
    }
}
