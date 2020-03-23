package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import  ch.usi.inf.sa4.sphinx.service.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;



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
                                                    @NotNull @RequestHeader("session-token") String sessionToken,
                                                    @NotNull @RequestHeader("user") String username,
                                                        Errors errors) {
        var res = check(sessionToken, username, errors, roomId);
        if (res != null) {
            return res;
        }


        return ResponseEntity.ok(new SerialisableRoom(roomService.get(roomId)));

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
                                              @NotNull @RequestHeader("session-token") String sessionToken,
                                              @NotNull @RequestHeader("user") String username,
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

    /**
     * Creates a new room.
     * @param serialisableRoom a new room
     * @param sessionToken session token of the user
     * @param username the username of the user
     * @param errors in case error occur
     * @return a new room
     */
    @PostMapping("/")
    public ResponseEntity<SerialisableRoom> createRoom(@NotNull @RequestBody SerialisableRoom serialisableRoom,
                                                       @NotBlank @RequestHeader("session-token") String sessionToken,
                                                       @NotBlank @RequestHeader("user") String username,
                                                       Errors errors) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        if(!userService.validSession(username, sessionToken)){
            return ResponseEntity.status(401).build();
        }

        Room room = new Room(serialisableRoom);

        String id = userService.addRoom(username, room);
        if (id == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.status(200).body(new SerialisableRoom(roomService.get(id)));
        }
    }

    /**
     * Changes the fields of given room.
     * @param roomId the id of the room
     * @param serialisableRoom a room with new fields
     * @param sessionToken session token of the user
     * @param username the username of the user
     * @param errors in case error occur
     * @return A modified room
     */
    @PutMapping("/{roomId}/")
    public ResponseEntity<SerialisableRoom> modifyRoom(@NotBlank @PathVariable String roomId,
                                                       @NotBlank @RequestBody SerialisableRoom serialisableRoom,
                                                       @NotBlank @RequestHeader("session-token") String sessionToken,
                                                       @NotBlank @RequestHeader("user") String username,
                                                       Errors errors) {
        var res = check(sessionToken, username, errors, roomId);
        if (res != null) {
            return res;
        }
        Room room = roomService.get(roomId);
        if (roomService.update(new Room(serialisableRoom))) {
            return ResponseEntity.status(203).body(new SerialisableRoom(room));
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    /**
     * Removes given room.
     * @param roomId the id of the room
     * @param sessionToken session token of the user
     * @param username the username of the user
     * @param errors in case error occur
     * @return  A ResponseEntity containing status code 203 if the room was removed
     *       status 403 if the delete went wrong
     */
    @DeleteMapping("/{roomId}/")
    public ResponseEntity<SerialisableRoom> deleteRoom (@NotBlank @PathVariable String roomId,
                                                        @NotBlank @RequestHeader("session-token") String sessionToken,
                                                        @NotBlank @RequestHeader("user") String username,
                                                        Errors errors) {
        var res = check(sessionToken, username, errors, roomId);
        if (res != null) {
            return res;
        }
        if (userService.removeRoom(username, roomId)) {
            return ResponseEntity.status(203).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    private ResponseEntity<SerialisableRoom> check(String sessionToken, String username, Errors errors, String roomId) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        if(!userService.validSession(username, sessionToken)){
            return ResponseEntity.status(401).build();
        }
        Room room = roomService.get(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return null;

    }
}

