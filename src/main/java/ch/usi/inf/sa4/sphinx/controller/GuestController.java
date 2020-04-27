package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.ServerErrorException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import ch.usi.inf.sa4.sphinx.model.Serialiser;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ch.usi.inf.sa4.sphinx.service.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/guests")
public class GuestController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserStorage userStorage;

    @Autowired
    private UserService deviceService;
    @Autowired
    private Serialiser serialiser;


    /**
     * Get all the guests of a certain user.
     *
     * @param username      the username of the user.
     * @param session_token the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of guests  or
     * 401 the user or the session-token aren't valid
     */
    @GetMapping("/")

    public ResponseEntity<SerialisableUser[]> getGuests(@RequestHeader("session-token") String session_token, @RequestHeader("username") String username) {


        Optional<User> user = userStorage.findByUsername(username);

        if (user.isPresent()) {
            if (!userService.validSession(username, "session_token")) {
                throw new UnauthorizedException("");
            }


            List<User> guest = userService.returnGuests(username);
            SerialisableUser[] users = new SerialisableUser[guest.size()];
            for (int i = 0; i < users.length; i++) {
                users[i] = serialiser.serialiseUser(guest.get(i));
            }
            return ResponseEntity.ok(users);
        }

        throw new ServerErrorException("");
    }


    /**
     * Get the list of houses the is allowed to access as guest.
     *
     * @param username      the username of the user.
     * @param session_token the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of user's houses the guest has access to
     */
    @GetMapping("/houses/")
    public ResponseEntity<SerialisableUser[]> getHouses(@RequestHeader("session-token") String session_token,
                                                        @RequestHeader("username") String username) {

        Optional<User> user = userStorage.findByUsername(username);

        if (user.isPresent()) {
            if (!userService.validSession(username, "session_token")) {
                throw new UnauthorizedException("");
            }
            List<User> guestOf = userService.getGuests(username);
            SerialisableUser[] users = new SerialisableUser[guestOf.size()];
            for (int i = 0; i < users.length; i++) {
                users[i] = serialiser.serialiseUser(guestOf.get(i));
            }
            return ResponseEntity.ok(users);


        }
        throw new ServerErrorException("");

    }


    /**
     * Get the list of houses the is allowed to access as guest.
     *
     * @param username      the username of the user.
     * @param session_token the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of user's houses the guest has access to
     */
    @GetMapping("/guests/{username}/devices/{guest_username}")
    public ResponseEntity<SerialisableDevice[]> getAuthorizedDevices(@NotNull @PathVariable("guest_username") String guest_username, @RequestHeader("session-token") String session_token,
                                                                     @RequestHeader("username") String username) {

        Optional<User> user = userStorage.findByUsername(username);

        if (user.isPresent()) {
            if (!userService.validSession(username, session_token)) {
                throw new UnauthorizedException("");
            }
            Optional<User> guest = userService.get(guest_username);
            if (guest == null) {
                return ResponseEntity.notFound().build();
            }


            Optional<List<Integer>> devicesIds = userService.getDevices(username);
            SerialisableDevice[] devices = new SerialisableDevice[devicesIds.get().size()];
            for (int i = 0; i < devices.length; i++) {
                //devices[i] = serialiser.serialiseDevice(devicesIds.get(i));
            }
            return ResponseEntity.ok(devices);
        }
        return null;
    }


// TODO: Implement scenes route



//    /**
//     *  Get all scenes a guest is authorized to access.
//     * @param username the username of the user.
//     * @param  session_token the session token used for validation
//     * @param guest_username a String representing the username of the guest
//     * @return a ResponseEntity with status code 203 and a body with the newly-created guest's data if the process was successful or
//     *        400 if some data was missing
//     */
//    @GetMapping("/{username}/scenes/{guest_username}")
//    public ResponseEntity<SerialisableUser> getAuthorizedScenes( @NotNull @RequestBody String guest_username,
//                                                                  @RequestHeader("session-token") String session_token,
//                                                                  @RequestHeader("username") String username) {
//
//        User user = userService.get(username);
//
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (session_token == null || !session_token.equals(user.getSessionToken())) {
//            return ResponseEntity.status(401).build();
//        }
//        return null;
//    }


    //TODO: should add scenes

    /**
     * Creates a new user.
     *
     * @param username       the username of the user.
     * @param session_token  the session token used for validation
     * @param guest_username a String representing the username of the guest
     * @return a ResponseEntity with status code 203 and a body with the newly-created guest's data if the process was successful or
     * 400 if some data was missing
     */
    @PostMapping("/")
    public ResponseEntity<SerialisableUser> createGuestOf(@NotBlank @RequestBody String guest_username,
                                                          @RequestHeader("session-token") String session_token,
                                                          @RequestHeader("username") String username) {

        Optional<User> user = userService.get(username);
        Optional<User> guestOf = userService.get(guest_username);

        if (user.isPresent()) {
            if (!userService.validSession(username, session_token)) {
                throw new UnauthorizedException("");
            }


            Optional<User> addedTo = userService.addGuest(username, guest_username);


            if (addedTo == null) {
                return ResponseEntity.status(500).build();
            } else {

                return ResponseEntity.status(201).body(serialiser.serialiseUser(userService.get(guest_username).get()));

            }


        }
        throw new ServerErrorException("");
    }


    /**
     * Deletes a guest.
     *
     * @param username       the user who want to delete a guest
     * @param guest_username the guest to delete
     * @param session_token  the session token used to authenticate
     * @return a ResponseEntity containing one of the following status codes:
     * 404 if no user with the given username exists
     * 401 if the session token does not match
     * 204 if the operation was successful
     */
    @DeleteMapping("/{guest_username}")
    public ResponseEntity<SerialisableUser> deleteGuestOf(@PathVariable("guest_username") String guest_username,
                                                          @RequestHeader("session-token") String session_token, @RequestHeader("username") String username) {
        Optional<User> user = userService.get(username);
        Optional<User> guestOf = userService.get(guest_username);

        if (user.isPresent()) {
            if (!userService.validSession(username, session_token)) {
                throw new UnauthorizedException("");
            }


            if (userService.removeGuest(username, guest_username)) {

                return ResponseEntity.status(204).build();
            } else {
                return ResponseEntity.status(500).build();
            }


        }

        return null;
    }


}
