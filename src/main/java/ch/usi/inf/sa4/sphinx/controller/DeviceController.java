package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    /**
     * @param sessionToken session token of the user
     * @return a ResponseEntity with the ids of the devices owned by the user
     */
    @GetMapping("/")
    public ResponseEntity<int[]> getUserDevices(@RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {
        User user = Storage.getUser(username);

        if (user != null) {
            if (user.getSessionToken() != sessionToken) {
                return ResponseEntity.status(401).build();
            }

            List<Integer> devices = user.getDevices();
            return ResponseEntity.ok(devices.toArray());
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * @param deviceId id of the device
     * @return a ResponseEntity with the data of the requested device (200), not found (404) if no such device exist
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<SerialisableDevice> getDevice(@PathVariable Integer deviceId, @RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {
        Device device = Storage.getDevice(deviceId);
        User user = Storage.getUser(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getSessionToken() != sessionToken) {
            return ResponseEntity.status(401).build();
        }

        if (device != null) {
            return ResponseEntity.ok(new SerialisableDevice(device, user));
        }


    }


    /**
     * @param device data of the device to be created, name, label and icon are required
     * @return a ResponseEntity with the data of the newly created device (203), or
     * 500 in case of a server error
     */
    @PostMapping("/")
    public ResponseEntity<SerialisableDevice> createDevice(@RequestBody SerialisableDevice device, @RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {
        User user = Storage.getUser(username);


        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getSessionToken() != sessionToken) {
            return ResponseEntity.status(401).build();
        }


        if (device.name == null || device.label == null || device.icon == null) {
            return ResponseEntity.badRequest().build();
        }


        //TODO differend kind of devices depending on the label, might benefit from a factory
        Device newDevice = null;


        //TODO make storage make the ids?
        newDevice.setId(UUID.randomUUID().toString());
        Storage.insertDevice(newDevice);

        return ResponseEntity.status(201).body(new SerialisableDevice(newDevice, user));

    }


    /**
     * @param deviceId id  of the device to be modified
     * @param name     new name
     * @param icon     new icon
     * @return a ResponseEntity with the data of the modified device (200), not found (404) if no such device exist or
     * 500 in case of a server error
     */
    @PutMapping("/{deviceId}")
    public ResponseEntity<Device> modifyDevice(@PathVariable Integer deviceId, @RequestBody String name, @RequestBody String icon) {
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getSessionToken() != sessionToken) {
            return ResponseEntity.status(401).build();
        }


        var devices = user.getDevices();
        for (Device device : devices) {
            if (device.getId() == deviceId) {
                if (name != null) {
                    device.setName(name);
                }
                if (icon != null) {
                    device.setIcon(icon);
                }

                Storage.insertDevice(device);

                return ResponseEntity.status(203).build();
            }
        }

        return ResponseEntity.notFound().build();

    }

    /**
     * @param deviceId id  of the device to be deleted
     * @return a ResponseEntity
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Device> deleteDevice(@PathVariable Integer deviceId, @RequestHeader("session-token") String sessionToken, @RequestHeader("user") String username) {

        User user = Storage.getUser(username);


        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getSessionToken() != sessionToken) {
            return ResponseEntity.status(401).build();
        }


        var devices = user.getDevices();
        for (Device device : devices) {
            if (device.getId() == deviceId) {
                Storage.deleteDevice(deviceId);
                return ResponseEntity.status(202).build();
            }
        }


        return ResponseEntity.notFound().build();

    }


}
