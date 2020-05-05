package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.model.*;
import ch.usi.inf.sa4.sphinx.service.*;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Controller for the /devices route
 */
@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/devices")
public class DeviceController {


    @Autowired
    CouplingService couplingService;
    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;
    @Autowired
    Serialiser serialiser;
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    /**
     * Gets the devices owned by a User.
     * @param sessionToken the session token of the User
     * @param username     the username of the User
     * @return a ResponseEntity with the ids of the devices owned by the user or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     * @see SerialisableDevice
     * @see Device
     */
    @GetMapping(value = {"", "/"})
    @ApiOperation(value = "Gets the devices owned by the User")
    public ResponseEntity<List<SerialisableDevice>> getUserDevices(@RequestHeader("session-token") String sessionToken,
                                                                   @RequestHeader("user") String username) {


        Optional<User> user = userService.get(username);

        if (user.isPresent()) {
            if (!userService.validSession(username, sessionToken)) {
                throw new UnauthorizedException("Invalid credentials");
            }

            List<Device> devices = userService.getPopulatedDevices(username).get();//if user exists optional is present
            List<SerialisableDevice> serializedDevices = devices.stream()
                    .map(device -> serialiser.serialiseDevice(device, user.get()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(serializedDevices);

        }
        throw new UnauthorizedException("Invalid credentials");

    }


    /**
     * Gets a device with a given Id.
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
    @GetMapping("/{deviceId}")
    @ApiOperation(value = "Gets the device with the given id")
    public ResponseEntity<SerialisableDevice> getDevice(@NotBlank @PathVariable Integer deviceId,
                                                        @RequestHeader("session-token") String sessionToken,
                                                        @RequestHeader("user") String username) {

        Optional<Device> device = deviceService.get(deviceId);

        if (device.isEmpty()) {
            throw new NotFoundException("No devices found");
        }

        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if  (!userService.ownsDevice(username, deviceId)) {
            throw new UnauthorizedException("You don't own this device");
        }

        return ResponseEntity.ok(serialiser.serialiseDevice(device.get()));
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
    @PostMapping(value = {"", "/"})
    @ApiOperation(value = "Creates a device")
    public ResponseEntity<SerialisableDevice> createDevice(@NotNull @RequestBody SerialisableDevice device,
                                                           @RequestHeader("session-token") String sessionToken,
                                                           @RequestHeader("user") String username,
                                                           Errors errors) {

        if (errors.hasErrors() || Objects.isNull(device.roomId) || Objects.isNull(device.type)) {
            throw new BadRequestException("Some fields are missing");
        }

        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!userService.ownsRoom(username, device.roomId)) {
            throw new UnauthorizedException("You don't own this room");
        }

        User user = userService.get(username).get(); //If the session is valid the User exists


        Integer deviceId = roomService.addDevice(device.roomId, DeviceType.intToDeviceType(device.type))
                .orElseThrow(() -> new ServerErrorException("Couldn't add device to room"));

        Device d = deviceService.get(deviceId).get(); //Since the previous exists then this does too


        if (device.icon != null && !device.icon.isBlank()) d.setIcon(device.icon);
        if (device.name != null && !device.name.isBlank()) d.setName(device.name);

        if (!deviceService.update(d)) throw new ServerErrorException("Couldn't save device data");

        return ResponseEntity.status(201).body(serialiser.serialiseDevice(deviceService.get(deviceId).get(), user));

    }

//

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
    @ApiOperation(value = "Modifies a Device")
    public ResponseEntity<SerialisableDevice> modifyDevice(@NotBlank @PathVariable Integer deviceId,
                                                           @NotBlank @RequestBody SerialisableDevice device,
                                                           @RequestHeader("session-token") String sessionToken,
                                                           @RequestHeader("user") String username,
                                                           Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("Some fields are missing");
        }

        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!userService.ownsDevice(username, deviceId)) {
            throw new UnauthorizedException("You don't own this device");
        }

        Device storageDevice = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException("No devices found"));

        User user = userService.get(username).get(); //exists if prev is valid

        if (device.icon != null) storageDevice.setIcon(device.icon);
        if (device.name != null) storageDevice.setName(device.name);

        if (device.on != null) storageDevice.setOn(device.on);
        if (storageDevice instanceof Dimmable && device.slider != null) {
            ((Dimmable) storageDevice).setState(device.slider);
        }
        if (storageDevice instanceof StatelessDimmableSwitch && device.slider != null) {
            ((StatelessDimmableSwitch) storageDevice).setIncrement(device.slider > 0);
        }


        if (deviceService.update(storageDevice)) {
            final Integer owningRoom = storageDevice.getRoom().getId();
            if (device.roomId != null && !device.roomId.equals(owningRoom)) {
                userService.migrateDevice(username, deviceId, owningRoom, device.roomId);
            }
            return ResponseEntity.ok().body(serialiser.serialiseDevice(storageDevice, user));

        }
        throw new ServerErrorException("Couldn't save data");
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
    @ApiOperation(value = "Resets a smartplug")
    public ResponseEntity<Boolean> resetSmartPlug(@PathVariable Integer deviceId,
                                                  @RequestHeader("session-token") String sessionToken,
                                                  @RequestHeader("user") String username) {
        Device plug = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException("No devices found"));


        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if  (!userService.ownsDevice(username, deviceId)) {
            throw new UnauthorizedException("You don't own this device");
        }

        if (!DeviceType.SMART_PLUG.equals(DeviceType.deviceToDeviceType(plug))) {
            throw new BadRequestException("Not a smart plug");
        }

        // safe because of the if statement immediately above this
        ((SmartPlug) plug).reset();

        if (!deviceService.update(plug)) {
            throw new ServerErrorException("Couldn't save data");
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * @param deviceId id  of the device to be deleted
     * @param sessionToken a session token that should match the User's
     * @param username the username of the User
     * @return a ResponseEntity with 204 if deletion is successful or
     * - 404 if not found or
     * - 401 if not authorized
     * @see User
     */
    @DeleteMapping("/{deviceId}")
    @ApiOperation(value = "Deletes the device with the given id")
    public ResponseEntity<Device> deleteDevice(@NotBlank @PathVariable Integer deviceId,
                                               @RequestHeader("session-token") String sessionToken,
                                               @RequestHeader("user") String username) {


        Device storageDevice = deviceService.get(deviceId).orElseThrow(() -> new NotFoundException("No devices found"));


        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if  (!userService.ownsDevice(username, deviceId)) {
            throw new UnauthorizedException("You don't own this device");
        }

        roomService.removeDevice(storageDevice.getRoom().getId(), storageDevice.getId());

        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a coupling between two devices.
     *
     * @param sessionToken the session token of the user to authenticate as
     * @param username     the username of the user to authenticate as
     * @param device1_id   id of the first device to couple
     * @param device2_id   id of the second device to couple
     * @return a ResponseEntity with 204 if coupling is successful or
     * - 404 if not found or
     * - 401 if not authorized or
     * - 500 in case of a server error
     * @see Coupling
     * @see Device
     * @see User
     */
    @PostMapping("/couple/{device1_id}/{device2_id}")
    @ApiOperation(value = "Creates a coupling between two devices")
    public ResponseEntity<SerialisableDevice> addCoupling(@RequestHeader("session-token") String sessionToken,
                                                          @RequestHeader("user") String username,
                                                          @PathVariable String device1_id,
                                                          @PathVariable String device2_id) {

        if (Objects.isNull(device1_id) || Objects.isNull(device2_id)) {
            throw new BadRequestException("Some fields are missing");
        }

        Integer id1 = Integer.parseInt(device1_id);
        Integer id2 = Integer.parseInt(device2_id);
        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if  (!userService.ownsDevice(username, id1) || !userService.ownsDevice(username, id2)) {
            throw new UnauthorizedException("You don't own one of the devices");
        }

        Device device1 = deviceService.get(id1).orElseThrow(() -> new NotFoundException("No devices found (1)"));
        Device device2 = deviceService.get(id2).orElseThrow(() -> new NotFoundException("No devices found (2)"));


        if (deviceService.createCoupling(device1, device2)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ServerErrorException("Couldn't save data");
        }
    }
    /**
     * Deletes a coupling between two devices.
     *
     * @param sessionToken the session token of the user to authenticate as
     * @param username     the username of the user to authenticate as
     * @param device1_id   id of the first device's couple to delete
     * @param device2_id   id of the second device's couple to delete
     * @return a ResponseEntity with 200 if deletion is successful or
     * - 404 if not found or
     * - 401 if not authorized or
     * - 500 in case of a server error
     */
    @DeleteMapping("/couple/{device1_id}/{device2_id}")
    public ResponseEntity<Boolean> removeCoupling(@RequestHeader("session-token") String sessionToken,
                                                  @RequestHeader("user") String username,
                                                  @PathVariable String device1_id,
                                                  @PathVariable String device2_id){

        if (Objects.isNull(device2_id) || Objects.isNull(device1_id)) {
            throw new BadRequestException("Some fields are missing");
        }

        Integer id1 = Integer.parseInt(device1_id);
        Integer id2 = Integer.parseInt(device2_id);

        if (!userService.validSession(username, sessionToken)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if  (!userService.ownsDevice(username, id1) || !userService.ownsDevice(username, id2)) {
            throw new UnauthorizedException("You don't own one of the devices");
        }

        deviceService.get(id1).orElseThrow(() -> new NotFoundException("No devices found (1)"));
        deviceService.get(id2).orElseThrow(() -> new NotFoundException("No devices found (2)"));


        couplingService.removeByDevicesIds(id1, id2);

        return ResponseEntity.ok().build();
    }
}
