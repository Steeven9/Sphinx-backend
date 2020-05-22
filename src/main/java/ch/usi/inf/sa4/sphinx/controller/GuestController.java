package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/guests")
public class GuestController {
    @Autowired
    private UserService userService;

    /**
     * Get all the guests of a certain user.
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of guests  or
     * 401 the user or the session-token aren't valid
     */
    @GetMapping({"", "/"})
    public ResponseEntity<SerialisableUser[]> getGuests(@RequestHeader("session-token") final String sessionToken,
                                                        @RequestHeader("user") final String username) {
        userService.validateSession(username, sessionToken);

        final List<User> guest = userService.returnOwnGuests(username);
        final SerialisableUser[] users;
        users = guest.stream().map(User::serialise).toArray(SerialisableUser[]::new);

        return ResponseEntity.ok(users);
    }


    /**
     * Get the list of houses the  user is allowed to access as guest.
     * @param username     the username of the user.
     * @param sessionToken the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of the users whose houses can be accessed as
     * guest by the user
     */
    @GetMapping({"/houses/", "/houses"})
    public ResponseEntity<List<SerialisableUser>> getHouses(@RequestHeader("session-token") final String sessionToken,
                                                            @RequestHeader("user") final String username){
        userService.validateSession(username, sessionToken);

        final List<User> guestOf = userService.getHosts(username);
        final List<SerialisableUser> users = guestOf.stream().map(User::serialiseAsHost).collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    /**
     * Get the list of devices the guests can access.
     *
     * @param username       the username of the guest.
     * @param host the username of the owner
     * @param sessionToken   the session token used for validation
     * @return a ResponseEntity with status code 200 and a body with the list of user's houses the guest has access to
     */
    @GetMapping({"/{owner_username}/devices/", "/{owner_username}/devices"})
    public ResponseEntity<SerialisableDevice[]> getAuthorizedDevices(
            @NotNull @PathVariable("owner_username") final String host,
            @RequestHeader("session-token") final String sessionToken,
            @RequestHeader("user") final String username) {
        final Optional<User> owner = userService.get(host);

        userService.validateSession(username, sessionToken);

        if (owner.isEmpty() || !userService.isGuestOf(host, username)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        final boolean camsVisible = owner.get().areCamsVisible();
        final List<Device> devices = userService.getPopulatedDevices(host).orElseThrow(WrongUniverseException::new);
        final SerialisableDevice[] devicesArray = devices.stream()
                .filter(device -> camsVisible || device.getDeviceType() != DeviceType.SECURITY_CAMERA)
                .map(device -> device.serialise()).toArray(SerialisableDevice[]::new);

        return ResponseEntity.ok(devicesArray);
    }

    /**
     * Adds the name of the user who wants the guest, to the list of the guest.
     *
     * @param username      the username of the user.
     * @param sessionToken  the session token used for validation
     * @param guestUsername a String representing the username who wants to add the former as guest guest
     * @return a ResponseEntity with status code 203 and a body with the newly-created guest's data if the process was successful or
     * 401 if unauthorized
     */
    @PostMapping({"", "/"})
    public ResponseEntity<SerialisableUser> createGuestOf(@RequestBody final SerialisableUser guestUsername,
                                                          @RequestHeader("session-token") final String sessionToken,
                                                          @RequestHeader("user") final String username,
                                                          final Errors errors) {
        if (errors.hasErrors()) {
            throw new BadRequestException("field missing");
        }

        final Optional<User> guest = userService.get(guestUsername.username);
        userService.validateSession(username, sessionToken);

        if (guest.isEmpty()) {
            throw new NotFoundException("This user doesn't exist");
        }

        userService.addGuest(guestUsername.username, username);
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
    @DeleteMapping({"/{guest_username}", "/{guest_username}/"})
    public ResponseEntity<SerialisableUser> deleteGuestOf(@PathVariable("guest_username") final String guest_username,
                                                          @RequestHeader("session-token") final String sessionToken,
                                                          @RequestHeader("user") final String username) {
        userService.validateSession(username, sessionToken);
        if (!userService.removeGuest(username, guest_username)) {
            throw new ServerErrorException("Couldn't save data");
        }
        return ResponseEntity.noContent().build();
    }
}

