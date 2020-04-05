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
     * @return a ResponseEntity with the ids of the devices owned by the user
     */
    @GetMapping("/")
    public ResponseEntity<SerialisableDevice[]> getUserDevices(@RequestHeader("session-token") String sessionToken,
                                                               @RequestHeader("user") String username
    ) {


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
     * @return a ResponseEntity with the data of the requested device (200), not found (404) if no such device exist
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<SerialisableDevice> getDevice(@NotBlank @PathVariable Integer deviceId,
                                                        @RequestHeader("session-token") String sessionToken,
                                                        @RequestHeader("user") String username,
                                                        Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }


        User user = userService.get(username);

        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        Device device = deviceService.get(deviceId);

        return ResponseEntity.ok(serialiser.serialiseDevice(device));
    }


    /**
     * @param device       data of the device to be created, name, label and icon are required
     * @param sessionToken sessionToken of the use
     * @param username     username of the user
     * @param errors       errors in validating the fields
     * @return a ResponseEntity with the data of the newly created device (203), or
     * 500 in case of a server error
     */
    @PostMapping("/")
    public ResponseEntity<SerialisableDevice> createDevice(@NotNull @RequestBody SerialisableDevice device,
                                                           @RequestHeader("session-token") String sessionToken,
                                                           @RequestHeader("user") String username,
                                                           Errors errors) {

        if (errors.hasErrors() || Objects.isNull(device.roomId) || Objects.isNull(device.type)) {
            return ResponseEntity.badRequest().build();
        }

        if (!userService.validSession(username, sessionToken)) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.get(username);
        Integer deviceId = roomService.addDevice(device.roomId, DeviceType.intToDeviceType(device.type));

        return ResponseEntity.status(201).body(new SerialisableDevice(deviceService.get(deviceId), user, userService));

    }


    /**
     * @param deviceId id  of the device to be modified
     * @param device   device to modify
     * @return a ResponseEntity with the data of the modified device (200), not found (404) if no such device exist or
     * 500 in case of a server error
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

        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.get(username);
        Device storageDevice = deviceService.get(deviceId);
        if (device.icon != null) storageDevice.setIcon(device.icon);
        if (device.name != null) storageDevice.setName(device.name);
        if (device.on != null) storageDevice.setOn(device.on);

        if (deviceService.update(storageDevice)) {
            final Integer owningRoom = userService.owningRoom(username, deviceId);
            if (!device.roomId.equals(owningRoom)) {
                userService.migrateDevice(username, deviceId, owningRoom, device.roomId);
            }
            return ResponseEntity.status(200).body(new SerialisableDevice(storageDevice, user, userService));
        }
        return ResponseEntity.status(500).build();
    }

    /**
     * @param deviceId id  of the device to be deleted
     * @return a ResponseEntity
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Device> deleteDevice(@NotBlank @PathVariable Integer deviceId,
                                               @RequestHeader("session-token") String sessionToken,
                                               @RequestHeader("user") String username,
                                               Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        if (!userService.ownsDevice(username, deviceId) || !userService.validSession(username, sessionToken)) {
            return ResponseEntity.status(401).build();
        }
        userService.removeDevice(username, deviceId);

        return ResponseEntity.status(200).build();
    }


}
