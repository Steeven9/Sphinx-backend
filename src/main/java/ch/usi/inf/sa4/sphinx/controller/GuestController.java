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
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/guests")
public class GuestController {
    @Autowired
    private UserService userService;

    @Autowired
    private Serialiser serialiser;


    /**
     * Get all the guests of a certain user.
     *
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of guests  or
     * 401 the user or the session-token aren't valid
     */
    @GetMapping(value = {"", "/"})

    public ResponseEntity<SerialisableUser[]> getGuests(@RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {


        Optional<User> user = userService.get(username);

        if (user.isPresent() && userService.validSession(username, sessionToken)) {

            List<User> guest = userService.getGuestsOf(username);
            SerialisableUser[] users;
            users = guest.toArray(SerialisableUser[]::new);

            return ResponseEntity.ok(users);
        }

        throw new UnauthorizedException("");
    }


    /**
     * Get the list of houses the  user is allowed to access as guest.
     *
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of the houses the user can access as guest
     */
    @GetMapping(value = {"/houses/", "/houses"})
    public ResponseEntity<SerialisableUser[]> getHouses(@RequestHeader("session-token") String sessionToken,
                                                        @RequestHeader("user") String username) {

        Optional<User> user = userService.get(username);

        if (user.isPresent() && userService.validSession(username, sessionToken)) {

            List<User> guestOf = userService.otherHousesAccess(username);
            SerialisableUser[] users ;
            users = guestOf.toArray(SerialisableUser[]::new);
            return ResponseEntity.ok(users);


        }
        throw new UnauthorizedException("");
    }


    /**
     * Get the list of devices the guests can access.
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of user's houses the guest has access to
     */
    @GetMapping(value = {"/{username}/devices/{guest_username}","/{username}/devices/{guest_username}/"})
    public ResponseEntity<SerialisableDevice[]> getAuthorizedDevices(@NotNull @PathVariable("guest_username") String guest_username, @RequestHeader("session-token") String sessionToken,
                                                                    @PathVariable @RequestHeader("user") String username) {


        Optional<User> user = userService.get(username);

        if (user.isPresent() && userService.validSession(username, sessionToken)) {

            Optional<User> guest = userService.get(guest_username);
            Optional<List<Integer>> devicesIds = userService.getDevices(username);
            if (guest.isPresent() && devicesIds.isPresent()) {

                SerialisableDevice[] devicesArray;
                 //still have to filter for access authorized devices
                List<Device> devices = userService.getPopulatedDevices(username).get();//if user exists optional is present
                List<SerialisableDevice> serializedDevices = devices.stream()
                        .map(device -> serialiser.serialiseDevice(device, user.get()))
                        .collect(Collectors.toList());

                devicesArray  = devices.toArray(SerialisableDevice[]::new);
                return ResponseEntity.ok(devicesArray);

            }

        }
        throw new UnauthorizedException("");
    }


//
//    /**
//     * Get the list of scenes the guests can access.
//     * @param username       the username of the user.
//     * @param sessionToken   the session token used for validation
//     * @param guest_username a String representing the username of the guest
//     * @return a ResponseEntity with status code 203 and a body with the newly-created guest's data if the process was successful or
//     * 401 if unauthorized
//     */
//    @GetMapping(value = {"/{username}/devices/{guest_username}","/{username}/devices/{guest_username}/"} )
//    public ResponseEntity<SerialisableScene[]> getAuthorizedScenes(@NotNull @PathVariable String guest_username,
//                                                                @RequestHeader("session-token") String sessionToken,
//                                                               @PathVariable @RequestHeader("user") String username) {
//
//        Optional<User> user = userService.get(username);
//
//        if (user.isPresent() && userService.validSession(username, sessionToken)) {
//
//            Optional<User> guest = userService.get(guest_username);
//            if (guest.isPresent()) {
//
//                Optional<List<Integer>> scenesIds = userService.getScenes(username);
//                SerialisableScene[] scenes ;
//                scenes = scenesIds.orElse(null).toArray(Serialisablescene:: new);
//                return ResponseEntity.ok(scenes);
//            }
//        }
//        throw new UnauthorizedException("");
//    }






    /**
     * Adds the name of the user who wants the guest, to the list of the guest.
     * @param username       the username of the user.
     * @param sessionToken  the session token used for validation
     * @param guest a String representing the username who wants to add the former as guest guest
     * @return a ResponseEntity with status code 203 and a body with the newly-created guest's data if the process was successful or
     * 401 if unauthorized
     */
    @PostMapping(value = {"", "/"})
    public ResponseEntity<SerialisableUser> createGuestOf(@RequestBody SerialisableUser guest,
                                                          @RequestHeader("session-token") String sessionToken,
                                                          @RequestHeader("user") String username) {
        Optional<User> guestUsername = userService.get(guest.username);
        Optional<User> user = userService.get(username);
        String guest_username = guest.username;

        if (user.isPresent() && guestUsername.isPresent() &&  userService.validSession(username, sessionToken)) {


            userService.addGuest(username, guest_username);
            return ResponseEntity.status(201).body(serialiser.serialiseUser(userService.get(guest_username).get()));

            }
        throw new UnauthorizedException("");
    }


    /**
     * Deletes a guest.
     * @param username       the user who want to delete a guest
     * @param guest_username the guest to delete
     * @param sessionToken  the session token used to authenticate
     * @return a ResponseEntity containing one of the following status codes:
     * 404 if no user with the given username exists
     * 401 if the session token does not match
     * 204 if the operation was successful
     */
    @DeleteMapping(value = {"/{guest_username}","/{guest_username}/"})
    public ResponseEntity<SerialisableUser> deleteGuestOf(@PathVariable("guest_username") String guest_username,
                                                          @RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {
        Optional<User> user = userService.get(username);


        if (user.isPresent() && userService.validSession(username, sessionToken)) {



            if (userService.removeGuest(username, guest_username)) {

                return ResponseEntity.status(204).build();
            } else {
                throw new ServerErrorException("");
            }


        }

        throw new UnauthorizedException("");
    }

}
