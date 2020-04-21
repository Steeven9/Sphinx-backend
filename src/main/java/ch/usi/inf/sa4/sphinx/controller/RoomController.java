package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.Serialiser;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;
    @Autowired
    Serialiser serialiser;

    /**
     * Returns a list of all rooms which belong to the given user.
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return a ResponseEntity with the array of rooms owned by the user
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Collection<SerialisableRoom>> getAllRooms(@NotNull @RequestHeader("session-token") String sessionToken,
                                                                    @NotNull @RequestHeader("user") String username) {

        Optional<User> user = userService.get(username);
        if (user.isEmpty()) {
            throw new NotFoundException("");
        }

        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("");
        }

        userService.getPopulatedRooms(username);

        return ResponseEntity.ok(serialiser.serialiseRoom(userService.getPopulatedRooms(username)));
    }


    /**
     * Returns a room with all details about it.
     *
     * @param roomId       the id of the room
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return the room with all details about it
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<SerialisableRoom> getRoom(@PathVariable Integer roomId,
                                                    @NotNull @RequestHeader("session-token") String sessionToken,
                                                    @NotNull @RequestHeader("user") String username) {

        check(sessionToken, username, null, roomId);
        return ResponseEntity.ok(serialiser.serialiseRoom(roomService.get(roomId).get()));

    }

    /**
     * Given the room, returns all the devices in this room.
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return an array of devices in given room
     */
    @GetMapping("/{roomId}/devices")
    public ResponseEntity<Collection<SerialisableDevice>> getDevice(@PathVariable Integer roomId,
                                                                           @NotNull @RequestHeader("session-token") String sessionToken,

                                                                           @NotNull @RequestHeader("user") String username) {
        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("");
        }
        User user = userService.get(username).get();//It exists from previous check

        Room room = roomService.get(roomId).orElseThrow(()->new NotFoundException(""));
        return ResponseEntity.ok(serialiser.serialiseDevice(room.getDevices(), user));
    }
//
//    /**
//     * Creates a new room.
//     * @param serialisableRoom a new room
//     * @param sessionToken session token of the user
//     * @param username the username of the user
//     * @param errors in case error occur
//     * @return a new room
//     */
//    @PostMapping(value = {"", "/"})
//    public ResponseEntity<SerialisableRoom> createRoom(@NotBlank @RequestHeader("session-token") String sessionToken,
//                                                       @NotBlank @RequestHeader("user") String username,
//                                                       @NotNull @RequestBody SerialisableRoom serialisableRoom,
//                                                       Errors errors) {
//        if(errors.hasErrors()){
//            return ResponseEntity.badRequest().build();
//        }
//        if(!userService.validSession(username, sessionToken)){
//            return ResponseEntity.status(401).build();
//        }
//
//        Room room = new Room(serialisableRoom);
//
//        Integer id = userService.addRoom(username, room);
//        if (id == null) {
//            return ResponseEntity.status(500).build();
//        } else {
//            return ResponseEntity.status(201).body(serialiser.serialiseRoom(roomService.get(id)));
//        }
//    }
//
//    /**
//     * Changes the fields of given room.
//     * @param roomId the id of the room
//     * @param serialisableRoom a room with new fields
//     * @param sessionToken session token of the user
//     * @param username the username of the user
//     * @param errors in case error occur
//     * @return A modified room
//     */
//    @PutMapping("/{roomId}")
//    public ResponseEntity<SerialisableRoom> modifyRoom(@NotBlank @PathVariable Integer roomId,
//                                                       @NotBlank @RequestHeader("session-token") String sessionToken,
//                                                       @NotBlank @RequestHeader("user") String username,
//                                                       @NotBlank @RequestBody SerialisableRoom serialisableRoom,
//                                                       Errors errors) {
//        var res = check(sessionToken, username, errors, roomId);
//        if (res != null) {
//            return res;
//        }
//        if (roomService.update(new Room(serialisableRoom))) {
//            Room storageRoom = roomService.get(roomId);
//            return ResponseEntity.status(200).body(serialiser.serialiseRoom(storageRoom));
//        } else {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    /**
//     * Removes given room.
//     * @param roomId the id of the room
//     * @param sessionToken session token of the user
//     * @param username the username of the user
//     * @return  A ResponseEntity containing status code 203 if the room was removed
//     *       status 403 if the delete went wrong
//     */
//    @DeleteMapping("/{roomId}")
//    public ResponseEntity<SerialisableRoom> deleteRoom (@NotBlank @PathVariable Integer roomId,
//                                                        @NotBlank @RequestHeader("session-token") String sessionToken,
//                                                        @NotBlank @RequestHeader("user") String username) {
//        var res = check(sessionToken, username, null, roomId);
//        if (res != null) {
//            return res;
//        }
//        if (userService.removeRoom(username, roomId)) {
//            return ResponseEntity.status(204).build();
//        } else {
//            return ResponseEntity.status(500).build();
//        }
//    }
//

    /**
     * Checks if the request parameters are correct.
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @param errors       in case error occur
     * @param roomId       the id of the room
     * @return null if correct, otherwise a ResponseEntity
     */
    private void check(String sessionToken, String username, Errors errors, Integer roomId) {
        if (errors != null && errors.hasErrors()) {
            throw new BadRequestException("");
        }
        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException();
        }

        Room room = roomService.get(roomId).isEmpty() {
            throw new NotFoundException("");
        }
    }
}

