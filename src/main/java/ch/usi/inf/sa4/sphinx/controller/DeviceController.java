package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.*;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


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

        List<Device> devices = userService.getPopulatedDevices(username);
        SerialisableDevice[] serializedDevices = devices.stream()
                .map(device -> serialiser.serialiseDevice(device, user))
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

        if (device.on != null) storageDevice.setOn(device.on);
        if (storageDevice instanceof Dimmable && device.slider != null) {
            ((Dimmable) storageDevice).setState(device.slider);
        }
        if (storageDevice instanceof StatelessDimmableSwitch && device.slider != null) {
            ((StatelessDimmableSwitch) storageDevice).setIncrement(device.slider > 0);
        }


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
     * resets the SmartPlug with the given deviceId,
     * iff the user is authenticating with the correct user/session-token pair
     * @param deviceId id  of the smart plug to be reset
     * @param username the username of the user to authenticate as
     * @param sessionToken the session token of the user to authenticate as
     * @return a ResponseEntity with status code 204 if operation is successful or
     *  - 401 if authentication fails or
     *  - 404 if no device with the given DeviceId exists or
     *  - 400 if the indicated device is not a SmartPlug or
     *  - 500 in case of an internal server error
     */
    @PutMapping("/reset/{deviceId}")
    public ResponseEntity<Boolean> resetSmartPlug(@PathVariable Integer deviceId,
                                                  @RequestHeader("session-token") String sessionToken,
                                                  @RequestHeader("user") String username) {
        Device plug = deviceService.get(deviceId);

        if (plug == null) return ResponseEntity.notFound().build();

        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        if (!DeviceType.SMART_PLUG.equals(DeviceType.deviceToDeviceType(plug))) {
            return ResponseEntity.badRequest().build();
        }

        // safe because of the if statement immediately above this
        ((SmartPlug) plug).reset();

        if (!deviceService.update(plug)) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.noContent().build();
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
     */
    @PostMapping("/couple/{device1_id}/{device2_id}")
    public ResponseEntity<SerialisableDevice> addCoupling(@RequestHeader("session-token") String sessionToken,
                                                          @RequestHeader("user") String username,
                                                          @PathVariable String device1_id,
                                                          @PathVariable String device2_id) {

        if (Objects.isNull(device1_id) || Objects.isNull(device2_id)) {
            return ResponseEntity.badRequest().build();
        }

        Integer id1 = Integer.parseInt(device1_id);
        Integer id2 = Integer.parseInt(device2_id);
        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, id1) || !userService.ownsDevice(username, id2)) {
            return ResponseEntity.status(401).build();
        }

        Device device1 = deviceService.get(id1);
        Device device2 = deviceService.get(id2);

        if (device1 == null || device2 == null) {
            return ResponseEntity.notFound().build();
        } else {
            if (deviceService.createCoupling(device1, device2)) {
                return ResponseEntity.status(204).build();
            } else {
                return ResponseEntity.status(500).build();
            }
        }
    }
}
