package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
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

@RestController
@RequestMapping("/devices")
public class DeviceController {


    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;

    /**
     * @param sessionToken session token of the user
     * @return a ResponseEntity with the ids of the devices owned by the user
     */
    @GetMapping("/")
    public ResponseEntity<Integer[]> getUserDevices(@NotNull @RequestHeader("session-token") String sessionToken,
                                                    @NotNull @RequestHeader("user") String username,
                                                    Errors errors) {


        if (errors.hasErrors()) {
            return ResponseEntity.status(401).build();
        }

        User user = Storage.getUser(username);
        if (user != null) {
            if (!user.getSessionToken().equals(sessionToken)) {
                return ResponseEntity.status(401).build();
            }

            List<Integer> devices = userService.getDevices(username);
            return ResponseEntity.ok(devices.stream().toArray(Integer[]::new));
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * @param deviceId id of the device
     * @return a ResponseEntity with the data of the requested device (200), not found (404) if no such device exist
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<SerialisableDevice> getDevice(@PathVariable Integer deviceId,
                                                        @RequestHeader("session-token") String sessionToken,
                                                        @RequestHeader("user") String username) {
        deviceService.get(deviceId);
        User user = Storage.getUser(username);

        if (!userService.validSession(username, sessionToken) || userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new SerialisableDevice(deviceService.get(deviceId), user));


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
                                                           @NotBlank @RequestHeader("session-token") String sessionToken,
                                                           @NotBlank @RequestHeader("user") String username,
                                                           Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        if (!userService.validSession(username, sessionToken)) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.get(username);
        Integer deviceId = roomService.addDevice(device.roomId, DeviceType.intToDeviceType(device.type));

        return ResponseEntity.status(201).body(new SerialisableDevice(deviceService.get(deviceId), user));

    }


    /**
     * @param deviceId id  of the device to be modified
     * @param name     new name
     * @param icon     new icon
     * @return a ResponseEntity with the data of the modified device (200), not found (404) if no such device exist or
     * 500 in case of a server error
     */
    @PutMapping("/{deviceId}")
    public ResponseEntity<SerialisableDevice> modifyDevice(@NotBlank @PathVariable Integer deviceId,
                                                           @NotBlank @RequestBody String name,
                                                           @NotBlank @RequestBody String icon,
                                                           @NotBlank @RequestHeader("session-token") String sessionToken,
                                                           @NotBlank @RequestHeader("user") String username,
                                                           Errors errors) {


        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        if (!userService.validSession(username, sessionToken) || !userService.ownsDevice(username, deviceId)) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.get(username);
        Device device = deviceService.get(deviceId);
        device.setIcon(icon);
        device.setName(name);


        if (deviceService.update(device)) {
            return ResponseEntity.status(201).body(new SerialisableDevice(deviceService.get(deviceId), user));
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
                                               @NotBlank @RequestHeader("user") String username,
                                               Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        if (!userService.ownsDevice(username, deviceId) || !userService.validSession(username, sessionToken)) {
            return ResponseEntity.status(401).build();
        }
        userService.removeDevice(username, deviceId);

        return ResponseEntity.status(202).build();
    }


}
