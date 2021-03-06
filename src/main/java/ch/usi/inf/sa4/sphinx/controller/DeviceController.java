package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.model.Coupling.BadCouplingException;
import ch.usi.inf.sa4.sphinx.model.Coupling.Coupling;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.SecurityCamera;
import ch.usi.inf.sa4.sphinx.model.SmartPlug;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.*;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Controller for the /devices route
 */
@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/devices")
public class DeviceController {

    private static final Set<DeviceType> TYPES_GUEST_CAN_EDIT =
            Set.of(DeviceType.LIGHT, DeviceType.DIMMABLE_LIGHT, DeviceType.SMART_CURTAIN);

    @Autowired
    CouplingService couplingService;
    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static final String NODEVICESFOUND = "No devices found";
    private static final String NOTOWNS = "You don't own this device";
    private static final String FIELDSMISSING = "Some fields are missing";
    private static final String DATANOTSAVED = "Couldn't save data";
    @Autowired
    private AutomationService automationService;

    /**
     * Gets the devices owned by a User.
     *
     * @param sessionToken the session token of the User
     * @param username     the username of the User
     * @return a ResponseEntity with the ids of the devices owned by the user or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see SerialisableDevice
     * @see Device
     */
    @GetMapping({"", "/"})
    @ApiOperation("Gets the devices owned by the User")
    public ResponseEntity<List<SerialisableDevice>> getUserDevices(@RequestHeader("session-token") final String sessionToken,
                                                                   @RequestHeader("user") final String username) {

        automationService.runQuantitySensors();
        userService.validateSession(username, sessionToken);
        userService.generateValue(username);

        final List<Device> devices = userService.getPopulatedDevices(username)
                .orElseThrow(WrongUniverseException::new);//if user exists optional is present

        final List<SerialisableDevice> serializedDevices = devices.stream()
                .map(Device::serialise)
                .collect(Collectors.toList());

        return ResponseEntity.ok(serializedDevices);
    }


    /**
     * Gets a device with a given Id.
     *
     * @param deviceId     id of the device
     * @param sessionToken a session token that should match the User's
     * @param username     the username of the User
     * @return a ResponseEntity with the data of the requested device (200) or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see Device
     * @see SerialisableDevice
     */
    @GetMapping("/{deviceId}")
    @ApiOperation("Gets the device with the given id")
    public ResponseEntity<SerialisableDevice> getDevice(@NotNull @PathVariable final Integer deviceId,
                                                        @RequestHeader("session-token") final String sessionToken,
                                                        @RequestHeader("user") final String username) {
        final Device device = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException(NODEVICESFOUND));

        userService.validateSession(username, sessionToken);

        final User owner = device.getRoom().getUser();
        final boolean isGuest = userService.get(username).orElseThrow(WrongUniverseException::new).getHosts().stream()
                .anyMatch(user -> user.getId().equals(owner.getId()));


        // This can be written as a single expression but I tried and it became way too long and convoluted.
        // I hope it's a bit more readable like this.
        if (!userService.ownsDevice(username, deviceId)) {
            if (!isGuest || (!TYPES_GUEST_CAN_EDIT.contains(device.getDeviceType())
                    && !(owner.areCamsVisible() && device.getDeviceType() == DeviceType.SECURITY_CAMERA))) {
                throw new UnauthorizedException(NOTOWNS);
            }

        }
        userService.generateValue(username);
        return ResponseEntity.ok(device.serialise());
    }

    /**
     * Gets a security camera video feed with a given Id.
     * @param deviceId id of the device
     * @param sessionToken a session token that should match the User's
     * @param username the username of the User
     * @return a ResponseEntity with the data of the requested device (200) or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see Device
     * @see SerialisableDevice
     */
    @GetMapping({"/video/{deviceId}", "/video/{deviceId}/"})
    @ApiOperation("Gets the video feed from security camera")
    public ResponseEntity<String> getVideoFeed(@NotNull @PathVariable final Integer deviceId,
                                                        @RequestHeader("session-token") final String sessionToken,
                                                        @RequestHeader("user") final String username) {
        final Device device = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException(NODEVICESFOUND));

        userService.validateSession(username, sessionToken);

        if (device.getDeviceType() != DeviceType.SECURITY_CAMERA) {
            throw new BadRequestException("Not a security camera");
        }

        final User owner = device.getRoom().getUser();


        final boolean isGuest = userService.get(username).orElseThrow(WrongUniverseException::new).getHosts().stream()
                .anyMatch(user -> user.getId().equals(owner.getId()));

        if (!(userService.ownsDevice(username, deviceId) || (owner.areCamsVisible() && isGuest))) {
            throw new UnauthorizedException(NOTOWNS);
        }


        userService.generateValue(username);
        return ResponseEntity.ok(((SecurityCamera)device).getVideo());
    }


    /**
     * Creates a new device given a SerialisableDevice containing its initial data and a user/sessionToken pair which
     * owns the room that the device should be created in.
     *
     * @param device       data of the device to be created, name, roomId, type and icon are required
     * @param sessionToken sessionToken of the user
     * @param username     username of the user
     * @param errors       errors in validating the fields
     * @return a ResponseEntity with the data of the newly created device (201), or
     * - 400 if bad request or
     * - 401 if not authorized or
     * - 500 if an internal server error occurred
     */
    @PostMapping({"", "/"})
    @ApiOperation("Creates a device")
    public ResponseEntity<SerialisableDevice> createDevice(@NotNull @RequestBody final SerialisableDevice device,
                                                           @RequestHeader("session-token") final String sessionToken,
                                                           @RequestHeader("user") final String username,
                                                           final Errors errors) {
        if (errors.hasErrors() || Objects.isNull(device.getRoomId()) || Objects.isNull(device.getType())) {
            throw new BadRequestException(FIELDSMISSING);
        }

        userService.validateSession(username, sessionToken);

        if (!userService.ownsRoom(username, device.getRoomId())) {
            throw new UnauthorizedException("You don't own this room");
        }

        final Integer deviceId = roomService.addDevice(device.getRoomId(), DeviceType.intToDeviceType(device.getType()))
                .orElseThrow(() -> new ServerErrorException("Couldn't add device to room"));
        final Device d = deviceService.get(deviceId).orElseThrow(WrongUniverseException::new); //Since the previous exists then this does too

        if (device.getIcon() != null && !device.getIcon().isBlank()) d.setIcon(device.getIcon());
        if (device.getName() != null && !device.getName().isBlank()) d.setName(device.getName());

        if (!deviceService.update(d)) throw new ServerErrorException("Couldn't save device data");

        userService.generateValue(username);
        final Device device1 = deviceService.get(deviceId).orElseThrow(WrongUniverseException::new);
        return ResponseEntity.status(201).body(device1.serialise());
    }

    /**
     * modifies the device with the given deviceId to conform to the fields in the given SerialisableDevice,
     * iff the user is authenticating with the correct user/session-token pair
     *
     * @param deviceId     the id of the device to be modified
     * @param device       an object representing the device to modify
     * @param sessionToken the session token of the user to authenticate as
     * @param username     the username of the user to authenticate as
     * @param errors       validation errors
     * @return a ResponseEntity with the data of the modified device and status code 200 if operation is successful or
     * - 400 if bad request or
     * - 404 if no such device exist or
     * - 401 if authentication fails or
     * - 500 in case of a server error
     * @see SerialisableDevice
     * @see Device
     */
    @PutMapping("/{deviceId}")
    @ApiOperation("Modifies a Device")
    public ResponseEntity<SerialisableDevice> modifyDevice(@NotNull @PathVariable final Integer deviceId,
                                                           @NotNull @RequestBody final SerialisableDevice device,
                                                           @RequestHeader("session-token") final String sessionToken,
                                                           @RequestHeader("user") final String username,
                                                           final Errors errors) {
        if (errors.hasErrors()) throw new BadRequestException(FIELDSMISSING);

        userService.validateSession(username, sessionToken);

        final Device storageDevice = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException(NODEVICESFOUND));

        final User owner = storageDevice.getRoom().getUser();
        final boolean isGuest = userService.get(username).orElseThrow(WrongUniverseException::new).getHosts().stream()
                .anyMatch(user -> user.getId().equals(owner.getId()));

        if (!userService.ownsDevice(username, deviceId)
                && !(isGuest && TYPES_GUEST_CAN_EDIT.contains(storageDevice.getDeviceType()))) {
            throw new UnauthorizedException(NOTOWNS);
        }

        storageDevice.setPropertiesFrom(device);

        if (!deviceService.update(storageDevice)) throw new ServerErrorException(DATANOTSAVED);

        final Integer owningRoom = storageDevice.getRoom().getId();
        if (device.getRoomId() != null && !device.getRoomId().equals(owningRoom)) {
            userService.migrateDevice(username, deviceId, owningRoom, device.getRoomId());
        }
        userService.generateValue(username);
        return ResponseEntity.ok().body(storageDevice.serialise());
    }

    /**
     * resets the SmartPlug with the given deviceId,
     * iff the user is authenticating with the correct user/session-token pair
     *
     * @param deviceId     id  of the smart plug to be reset
     * @param username     the username of the user to authenticate as
     * @param sessionToken the session token of the user to authenticate as
     * @return a ResponseEntity with status code 204 if operation is successful or
     * - 401 if authentication fails or
     * - 404 if no device with the given DeviceId exists or
     * - 400 if the indicated device is not a SmartPlug or
     * - 500 in case of an internal server error
     * @see SmartPlug
     * @see User
     */
    @PutMapping("/reset/{deviceId}")
    @ApiOperation("Resets a smartplug")
    public ResponseEntity<Boolean> resetSmartPlug(@NotNull @PathVariable final Integer deviceId,
                                                  @NotNull @RequestHeader("session-token") final String sessionToken,
                                                  @RequestHeader("user") final String username) {

        final Device plug = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException(NODEVICESFOUND));

        userService.validateSession(username, sessionToken);


        if (!userService.ownsDevice(username, deviceId)) throw new UnauthorizedException(NOTOWNS);

        if (plug.getDeviceType() != DeviceType.SMART_PLUG) {
            throw new BadRequestException("Not a smart plug");
        }

        // safe because of the if statement immediately above this
        ((SmartPlug) plug).reset();

        if (!deviceService.update(plug)) throw new ServerErrorException(DATANOTSAVED);

        return ResponseEntity.noContent().build();
    }



    /**
     * @param deviceId     id  of the device to be deleted
     * @param sessionToken a session token that should match the User's
     * @param username     the username of the User
     * @return a ResponseEntity with 204 if deletion is successful or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     */
    @DeleteMapping("/{deviceId}")
    @ApiOperation("Deletes the device with the given id")
    public ResponseEntity<Device> deleteDevice(@NotNull @PathVariable final Integer deviceId,
                                               @RequestHeader("session-token") final String sessionToken,
                                               @RequestHeader("user") final String username) {
        final Device storageDevice = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException(NODEVICESFOUND));

        userService.validateSession(username, sessionToken);


        if (!userService.ownsDevice(username, deviceId)) throw new UnauthorizedException(NOTOWNS);

        roomService.removeDevice(storageDevice.getRoom().getId(), storageDevice.getId());

        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a coupling between two devices. The order of the ids should not matter.
     *
     * @param sessionToken the session token of the user to authenticate as
     * @param username     the username of the user to authenticate as
     * @param id1          id of the first device to couple
     * @param id2          id of the second device to couple
     * @return a ResponseEntity with 204 if coupling is successful or
     * - 404 if not found or
     * - 401 if not authorized or
     * - 500 in case of a server error
     * @see Coupling
     * @see Device
     * @see User
     */
    @PostMapping("/couple/{device1_id}/{device2_id}")
    @ApiOperation("Creates a coupling between two devices")
    public ResponseEntity<SerialisableDevice> addCoupling(@RequestHeader("session-token") final String sessionToken,
                                                          @RequestHeader("user") final String username,
                                                          @NonNull @PathVariable(name = "device1_id") final Integer id1,
                                                          @NonNull @PathVariable(name = "device2_id") final Integer id2) {
        userService.validateSession(username, sessionToken);

        if (!userService.ownsDevice(username, id1) || !userService.ownsDevice(username, id2)) {
            throw new UnauthorizedException("You don't own one of the devices");
        }

        final Device device1 = deviceService.get(id1).orElseThrow(() -> new NotFoundException(NODEVICESFOUND + " (1)"));
        final Device device2 = deviceService.get(id2).orElseThrow(() -> new NotFoundException(NODEVICESFOUND + " (2)"));


        try {
            couplingService.createCoupling(device1, device2);
            return ResponseEntity.noContent().build();
        } catch (BadCouplingException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
    }

    /**
     * Deletes a coupling between two devices.
     *
     * @param sessionToken the session token of the user to authenticate as
     * @param username     the username of the user to authenticate as
     * @param id1          id of the first device's couple to delete
     * @param id2          id of the second device's couple to delete
     * @return a ResponseEntity with 200 if deletion is successful or
     * - 404 if not found or
     * - 401 if not authorized or
     * - 500 in case of a server error
     */
    @DeleteMapping("/couple/{device1_id}/{device2_id}")
    public ResponseEntity<Boolean> removeCoupling(@RequestHeader("session-token") final String sessionToken,
                                                  @RequestHeader("user") final String username,
                                                  @NotNull @PathVariable(name = "device1_id") final Integer id1,
                                                  @NotNull @PathVariable(name = "device2_id") final Integer id2) {

        userService.validateSession(username, sessionToken);

        if (!userService.ownsDevice(username, id1) || !userService.ownsDevice(username, id2)) {
            throw new UnauthorizedException("You don't own one of the devices");
        }

        couplingService.removeByDevicesIds(id1, id2);
//        couplingService.removeByDevicesIds(id2, id1);

        return ResponseEntity.ok().build();
    }
}
