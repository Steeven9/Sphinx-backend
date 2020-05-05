package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.Serialiser;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/rooms")
@Validated
@Api(value = "Room endpoint", description = "Room Controller")
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
     * @see Room
     * @see SerialisableRoom
     */
    @GetMapping(value = {"", "/"})
    @ApiOperation(value = "Returns all the rooms owned by the User")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 404, message = "if the User is not found"),
                   // @ApiResponse(code = 401, message = "if the Auth is not valid")
            }
    )
    public ResponseEntity<List<SerialisableRoom>> getAllRooms(@NotNull @RequestHeader("session-token") String sessionToken,
                                                              @NotNull @RequestHeader("user") String username) {

        check(sessionToken, username,null);

        return ResponseEntity.ok(serialiser.serialiseRooms(userService.getPopulatedRooms(username)));
    }


    /**
     * Returns all the info regarding a given Room.
     *
     * @param roomId       the id of the room
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return a SerialisableRoom containing info of the requested Room
     * @see SerialisableRoom
     * @see Room
     */
    @GetMapping("/{roomId}")
    @ApiOperation(value = "Returns a Room given its id")
    public ResponseEntity<SerialisableRoom> getRoom(@PathVariable Integer roomId,
                                                    @NotNull @RequestHeader("session-token") String sessionToken,
                                                    @NotNull @RequestHeader("user") String username) {
        check(sessionToken, username, null, roomId);
        //if check didn't throw the room is here
        return ResponseEntity.ok(serialiser.serialiseRoom(roomService.get(roomId).get()));

    }

    /**
     * Given the id of a Room, returns all the info of the Devices in it.
     *
     * @param roomId       the id of the Room
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return an array of devices in given room
     * @see Room
     * @see SerialisableDevice
     */
    @GetMapping("/{roomId}/devices")
    @ApiOperation(value = "Returns all devices in the given Room")
    public ResponseEntity<Collection<SerialisableDevice>> getDevice(@PathVariable Integer roomId,
                                                                    @NotNull @RequestHeader("session-token") String sessionToken,
                                                                    @NotNull @RequestHeader("user") String username) {

        check(sessionToken, username, null, roomId);

        User user = userService.get(username).orElseThrow(()->new ServerErrorException("The universe broke"));//It exists from previous check
        Room room = roomService.get(roomId).orElseThrow(() -> new ServerErrorException("The universe broke"));//It exists from previous check

        return ResponseEntity.ok(serialiser.serialiseDevices(room.getDevices(), user));
    }


    /**
     * Creates a new room.
     *
     * @param sessionToken     session token of the user
     * @param username         the username of the user
     * @param serialisableRoom a new room
     * @param errors           in case error occur
     * @return a new room
     */
    @PostMapping(value = {"", "/"})
    @ApiOperation(value = "Creates a Room")
    public ResponseEntity<SerialisableRoom> createRoom(@NotBlank @RequestHeader("session-token") String sessionToken,
                                                       @NotBlank @RequestHeader("user") String username,
                                                       @NotNull @RequestBody SerialisableRoom serialisableRoom,
                                                       Errors errors) {


        check(sessionToken, username, errors);

        Room room = new Room(serialisableRoom);
        Integer id = userService.addRoom(username, room).orElseThrow(() -> new ServerErrorException("Couldn't save data"));
        SerialisableRoom res = serialiser.serialiseRoom(roomService.get(id).orElseThrow(
                () -> new ServerErrorException("Couldn't serialise room")
        ));

        return ResponseEntity.status(201).body(res);

    }


    /**
     * Changes the fields of given room.
     *
     * @param roomId           the id of the room
     * @param sessionToken     session token of the user
     * @param username         the username of the user
     * @param serialisableRoom a room with new fields
     * @param errors           in case error occur
     * @return A modified room
     */
    @PutMapping("/{roomId}")
    @ApiOperation(value = "Modifies a Room")
    public ResponseEntity<SerialisableRoom> modifyRoom(@NotBlank @PathVariable Integer roomId,
                                                       @NotBlank @RequestHeader("session-token") String sessionToken,
                                                       @NotBlank @RequestHeader("user") String username,
                                                       @NotBlank @RequestBody SerialisableRoom serialisableRoom,
                                                       Errors errors) {
        check(sessionToken, username, errors, roomId);

        Room storageRoom = roomService.get(roomId).orElseThrow(() -> new ServerErrorException("The universe broke"));


        String newName = serialisableRoom.name;
        String newIcon = serialisableRoom.icon;
        String newBackground = serialisableRoom.background;

        if (newName != null) {
            storageRoom.setName(newName);

        }
        if (newIcon != null) {
            storageRoom.setIcon(newIcon);
        }
        if (newBackground != null) {
            storageRoom.setBackground(newBackground);
        }

        if (!roomService.update(storageRoom)) {
            throw new ServerErrorException("Couldn't save data");
        }

        SerialisableRoom res = serialiser.serialiseRoom(storageRoom);
        return ResponseEntity.ok().body(res);
    }

    /**
     * Removes given room.
     *
     * @param roomId       the id of the room
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @return A ResponseEntity containing status code 203 if the room was removed
     * status 403 if the delete went wrong
     */
    @DeleteMapping("/{roomId}")
    @ApiOperation(value = "Deletes a Room")
    public ResponseEntity<SerialisableRoom> deleteRoom(@NotNull @PathVariable Integer roomId,
                                                       @NotBlank @RequestHeader("session-token") String sessionToken,
                                                       @NotBlank @RequestHeader("user") String username) {
        check(sessionToken, username, null, roomId);

        if (!userService.removeRoom(username, roomId)) {
           throw new ServerErrorException("Couldn't save data");
        }

        return ResponseEntity.noContent().build();
    }




    /**
     * Checks if the request parameters are correct. Throws if they are not.
     * It will check that:
     * there are no validation errors
     * the User exists and has a valid sessionToken
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @param errors       in case error occur
     */
    private void check(String sessionToken, String username, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            throw new BadRequestException(errors.getAllErrors().toString());
        }
        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }


    }

    /**
     * Checks if the request parameters are correct. Throws if they are not.
     * It will check that:
     * there are no validation errors
     * the User exists and has a valid sessionToken
     * the room with the given id belongs to the user
     *
     * @param sessionToken session token of the user
     * @param username     the username of the user
     * @param errors       in case error occur
     * @param roomId       the id of the room
     */
    private void check(String sessionToken, String username, Errors errors, Integer roomId) {
        check(sessionToken, username, errors);

        if(!userService.ownsRoom(username, roomId)) throw new UnauthorizedException("You don't own this room");

    }


}


