package ch.usi.inf.sa4.sphinx.controller;

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
     * @return a ResponseEntity with the ids of the rooms owned by the user
     */
    @GetMapping("/")
    public ResponseEntity<String[]> getAllRooms(@NotNull @RequestHeader("session-token") String sessionToken,
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

            List<String> rooms = user.getRooms();
            return ResponseEntity.ok(rooms.stream().toArray(String[]::new));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<SerialisableRoom> getRoom(@PathVariable String roomId,
                                                      @RequestHeader("session-token") String sessionToken,
                                                      @RequestHeader("user") String username) {
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

    @GetMapping("/{roomId}/devices/")
    public ResponseEntity<String[]> getDevice(@PathVariable String roomId,
                                                        @RequestHeader("session-token") String sessionToken,
                                                        @RequestHeader("user") String username
                                                        ) {
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
        List<String> list = room.getDevices();

        return ResponseEntity.ok(list.stream().toArray(String[]::new));
    }
}
