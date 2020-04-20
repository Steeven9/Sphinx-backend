package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Serialiser;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/devices")
public class DeviceController {


    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;
    @Autowired
    Serialiser serialiser;


    /**
     * @param sessionToken session token of the user
     * @return a ResponseEntity with the ids of the devices owned by the user or
     * - 404 if not found or
     * - 401 if not authorized
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<SerialisableDevice[]> getUserDevices(@RequestHeader("session-token") String sessionToken,
                                                               @RequestHeader("user") String username) {


        User user = userService.get(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!userService.validSession(username, sessionToken)) {
            return ResponseEntity.status(401).build();
        }

        List<Integer> devicesIds = userService.getDevices(username);
        SerialisableDevice[] serializedDevices = devicesIds.stream()
                .map(id -> serialiser.serialiseDevice(deviceService.get(id), user))
                .toArray(SerialisableDevice[]::new);
        
        return ResponseEntity.ok(serializedDevices);
    }


    /**
     * @param deviceId id of the device
     * @return a ResponseEntity with the data of the requested device (200) or
     * - 404 if not found or
     * - 401 if not authorized
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<SerialisableDevice> getDevice(@NotBlank @PathVariable Integer deviceId,
                                                        @RequestHeader("session-token") String sessionToken,
                                                        @RequestHeader("user") String username) {

        Device device = deviceService.get(deviceId);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }

        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(serialiser.serialiseDevice(device));
    }


    /**
     * Creates a new device given a SerialisableDevice containing its initial data and a user/sessionToken pair which
     * owns the room that the device should be created in.
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
    public ResponseEntity<SerialisableDevice> createDevice(@NotNull @RequestBody SerialisableDevice device,
                                                           @RequestHeader("session-token") String sessionToken,
                                                           @RequestHeader("user") String username,
                                                           Errors errors) {

        if (errors.hasErrors() || Objects.isNull(device.roomId) || Objects.isNull(device.type)) {
            return ResponseEntity.badRequest().build();
        }

        if (!userService.validSession(username, sessionToken) || !userService.ownsRoom(username, device.roomId)) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.get(username);
        Integer deviceId = roomService.addDevice(device.roomId, DeviceType.intToDeviceType(device.type));
        Device d = deviceService.get(deviceId);
        if (d == null) return ResponseEntity.status(500).build();
        if (device.icon != null && !device.icon.isBlank()) d.setIcon(device.icon);
        if (device.name != null && !device.name.isBlank()) d.setName(device.name);
        if (!deviceService.update(d)) return ResponseEntity.status(500).build();

        return ResponseEntity.status(201).body(serialiser.serialiseDevice(deviceService.get(deviceId), user));

    }


    /**
     * modifies the device with the given deviceId to conform to the fields in the given SerialisableDevice,
     * iff the user is authenticating with the correct user/session-token pair
     * @param deviceId id  of the device to be modified
     * @param device device to modify
     * @param username the username of the user to authenticate as
     * @param sessionToken the session token of the user to authenticate as
     * @return a ResponseEntity with the data of the modified device and status code 200 if operation is successful or
     *  - 400 if bad request or
     *  - 404 if no such device exist or
     *  - 401 if authentication fails or
     *  - 500 in case of a server error
     */
    @PutMapping("/{deviceId}")
    public ResponseEntity<SerialisableDevice> modifyDevice(@NotBlank @PathVariable Integer deviceId,
                                                           @NotBlank @RequestBody SerialisableDevice device,
                                                           @RequestHeader("session-token") String sessionToken,
                                                           @RequestHeader("user") String username,
                                                           Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Device storageDevice = deviceService.get(deviceId);

        if (storageDevice == null) {
            return ResponseEntity.notFound().build();
        }

        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.get(username);

        if (device.icon != null) storageDevice.setIcon(device.icon);
        if (device.name != null) storageDevice.setName(device.name);
        if (device.on != null) storageDevice.setActive(device.on);

        if (deviceService.update(storageDevice)) {
            final Integer owningRoom = userService.owningRoom(username, deviceId);
            if (device.roomId != null && !device.roomId.equals(owningRoom)) {
                userService.migrateDevice(username, deviceId, owningRoom, device.roomId);
            }
            return ResponseEntity.status(200).body(serialiser.serialiseDevice(storageDevice, user));

        }
        return ResponseEntity.status(500).build();
    }

    /**
     * @param deviceId id  of the device to be deleted
     * @return a ResponseEntity with 204 if deletion is successful or
     * - 404 if not found or
     * - 401 if not authorized
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Device> deleteDevice(@NotBlank @PathVariable Integer deviceId,
                                               @RequestHeader("session-token") String sessionToken,
                                               @RequestHeader("user") String username) {

        Device storageDevice = deviceService.get(deviceId);

        if (storageDevice == null) {
            return ResponseEntity.notFound().build();
        }

        if (!userService.ownsDevice(username, deviceId) || !userService.validSession(username, sessionToken)) {
            return ResponseEntity.status(401).build();
        }
        userService.removeDevice(username, deviceId);

        return ResponseEntity.status(204).build();
    }


}
