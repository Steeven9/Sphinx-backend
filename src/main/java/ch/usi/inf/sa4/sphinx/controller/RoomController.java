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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping({"", "/"})
    @ApiOperation("Returns all the rooms owned by the User")
    @ApiResponses(
            {
                    @ApiResponse(code = 404, message = "if the User is not found"),
                   // @ApiResponse(code = 401, message = "if the Auth is not valid")
            }
    )
    public ResponseEntity<List<SerialisableRoom>> getAllRooms(@NotNull @RequestHeader("session-token") final String sessionToken,
                                                              @NotNull @RequestHeader("user") final String username) {

        check(sessionToken, username,null);

        return ResponseEntity.ok(Serialiser.serialiseRooms(userService.getPopulatedRooms(username)));
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
    @ApiOperation("Returns a Room given its id")
    public ResponseEntity<SerialisableRoom> getRoom(@NotNull @PathVariable final Integer roomId,
                                                    @NotNull @RequestHeader("session-token") final String sessionToken,
                                                    @NotNull @RequestHeader("user") final String username) {
        check(sessionToken, username, null, roomId);
        //if check didn't throw the room is here
        return ResponseEntity.ok(Serialiser.serialiseRoom(roomService.get(roomId).orElseThrow(WrongUniverseException::new)));

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
    @ApiOperation("Returns all devices in the given Room")
    public ResponseEntity<Collection<SerialisableDevice>> getDevice(@NotNull @PathVariable final Integer roomId,
                                                                    @NotNull @RequestHeader("session-token") final String sessionToken,
                                                                    @NotNull @RequestHeader("user") final String username) {

        check(sessionToken, username, null, roomId);
        //regen

        final Room room = roomService.get(roomId).orElseThrow(WrongUniverseException::new);//It exists from previous check

        return ResponseEntity.ok(serialiser.serialiseDevices(room.getDevices()));
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
    @PostMapping({"", "/"})
    @ApiOperation("Creates a Room")
    public ResponseEntity<SerialisableRoom> createRoom(@NotNull @RequestHeader("session-token") final String sessionToken,
                                                       @NotNull @RequestHeader("user") final String username,
                                                       @NotNull @RequestBody final SerialisableRoom serialisableRoom,
                                                       final Errors errors) {


        check(sessionToken, username, errors);

        final Room room = new Room(serialisableRoom);
        final Integer id = userService.addRoom(username, room).orElseThrow(() -> new ServerErrorException("Couldn't save data"));
        final SerialisableRoom res = Serialiser.serialiseRoom(roomService.get(id).orElseThrow(
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
    @ApiOperation("Modifies a Room")
    public ResponseEntity<SerialisableRoom> modifyRoom(@NotNull @PathVariable final Integer roomId,
                                                       @NotNull @RequestHeader("session-token") final String sessionToken,
                                                       @NotNull @RequestHeader("user") final String username,
                                                       @NotNull @RequestBody final SerialisableRoom serialisableRoom,
                                                       final Errors errors) {
        check(sessionToken, username, errors, roomId);

        final Room storageRoom = roomService.get(roomId).orElseThrow(() -> new NotFoundException("No rooms found"));


        final String newName = serialisableRoom.name;
        final String newIcon = serialisableRoom.icon;
        final String newBackground = serialisableRoom.background;

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

        final SerialisableRoom res = Serialiser.serialiseRoom(storageRoom);
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
    @ApiOperation("Deletes a Room")
    public ResponseEntity<SerialisableRoom> deleteRoom(@NotNull @PathVariable final Integer roomId,
                                                       @NotNull @RequestHeader("session-token") final String sessionToken,
                                                       @NotNull @RequestHeader("user") final String username) {
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
    private void check(final String sessionToken, final String username, final Errors errors) {
        if (errors != null && errors.hasErrors()) {
            throw new BadRequestException(errors.getAllErrors().toString());
        }

        userService.validateSession(username, sessionToken);
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
    private void check(final String sessionToken, final String username, final Errors errors, final Integer roomId) {
        check(sessionToken, username, errors);

        if(!userService.ownsRoom(username, roomId)) throw new UnauthorizedException("You don't own this room");

    }


}


