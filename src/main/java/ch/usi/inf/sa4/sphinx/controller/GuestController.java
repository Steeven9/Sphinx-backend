package ch.usi.inf.sa4.sphinx.controller;



import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
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
    private UserStorage userStorage;

    @Autowired
    private UserService deviceService;

    @Autowired

    private Serialiser serialiser;


    /**
     * Get all the guests of a certain user.
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of guests  or
     * 401 the user or the session-token aren't valid
     */
    @GetMapping(value = {"", "/"})

    public ResponseEntity<SerialisableUser[]> getGuests(@RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {





        if ( !userService.validSession(username, sessionToken)) {

            throw new UnauthorizedException("Invalid credentials");


        }
        List<User> guest = userService.getGuestsOf(username);
        SerialisableUser[] users;
        users = guest.stream().map(user ->serialiser.serialiseUser(user)).toArray(SerialisableUser[]::new);


        return ResponseEntity.ok(users);


    }


    /**
     * Get the list of houses the  user is allowed to access as guest.
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of the users whose houses can be accessed as
     * guest by the user
     */

    @GetMapping(value = {"/houses/", "/houses"})
    public ResponseEntity<List<SerialisableUser>> getHouses(@RequestHeader("session-token") String sessionToken,
                                                            @RequestHeader("user") String username) {





        if ( !userService.validSession(username, sessionToken)) {



            throw new UnauthorizedException("Invalid credentials");



        }


        List<User> guestOf = userService.otherHousesAccess(username).get();
        List<SerialisableUser> users = guestOf.stream().map(user -> user.serialiseAsHost()).collect(Collectors.toList());






        return ResponseEntity.ok(users);


    }


    /**
     * Get the list of devices the guests can access.\
     *
     * @param username       the username of the guest.
     * @param host the username of the owner
     * @param sessionToken   the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of user's houses the guest has access to
     */


    @GetMapping(value = {"/{owner_username}/devices/", "/{owner_username}/devices"})
    public ResponseEntity<SerialisableDevice[]> getAuthorizedDevices
    (@NotNull @PathVariable("owner_username") String host, @RequestHeader("session-token") String
            sessionToken,
     @RequestHeader("user") String username) {



        Optional<User> user = userService.get(username);
        Optional<User> owner = userService.get(host);




        if (!userService.validSession(username, sessionToken)  || !owner.isPresent()) {
            throw new UnauthorizedException("Invalid credential");
        }

        boolean camsVisible = owner.get().areCamsVisible();
        List<Device> devices = userService.getPopulatedDevices(host).get();//if user exists optional is present
        SerialisableDevice[] devicesArray;
        if (camsVisible) {



            devicesArray = devices.stream()
                    .map(device -> serialiser.serialiseDevice(device, user.get())).toArray(SerialisableDevice[]::new);
        } else {

            // filter all devices except cams
            devicesArray = devices.stream()
                    .filter(device -> !(device.getDeviceType() == DeviceType.SECURITY_CAMERA))
                    .map(device -> serialiser.serialiseDevice(device, user.get())).toArray(SerialisableDevice[]::new);

        }



        return ResponseEntity.ok(devicesArray);

    }



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
     *
     * @param username      the username of the user.
     * @param sessionToken  the session token used for validation
     * @param guestUsername a String representing the username who wants to add the former as guest guest
     * @return a ResponseEntity with status code 203 and a body with the newly-created guest's data if the process was successful or
     * 401 if unauthorized
     */
    @PostMapping(value = {"", "/"})
    public ResponseEntity<SerialisableUser> createGuestOf(@RequestBody String guestUsername,
                                                          @RequestHeader("session-token") String sessionToken,
                                                          @RequestHeader("user") String username) {

        Optional<User> guest = userService.get(guestUsername);
        if (!userService.validSession(username, sessionToken)) {

            throw new UnauthorizedException("Invalid credentials");

        }

        if(!guest.isPresent()){
            throw new NotFoundException("This user doesn't exist");
        }


        userService.addGuest(guestUsername, username);
        return ResponseEntity.status(201).build();


    }


    /**
     * Deletes a guest.

     *
     * @param username       the user who want to delete a guest
     * @param guest_username the guest to delete
     * @param sessionToken   the session token used to authenticate
     * @return a ResponseEntity containing one of the following status codes:
     * 404 if no user with the given username exists
     * 401 if the session token does not match
     * 204 if the operation was successful
     */



    @DeleteMapping(value = {"/{guest_username}", "/{guest_username}/"})
    public ResponseEntity<SerialisableUser> deleteGuestOf(@PathVariable("guest_username") String
                                                                  guest_username, @RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {





        if (  !userService.validSession(username, sessionToken)) {


            throw new UnauthorizedException("Invalid credentials");


        }
        if (!userService.removeGuest(username, guest_username)) {

            throw new ServerErrorException("Couldn't save data");
        } else {

            return ResponseEntity.noContent().build();
        }


    }

}

