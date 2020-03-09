package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {


    /**
     * @param sessionToken session token of the user
     * @return a ResponseEntity with the ids of the devices owned by the user
     */
    @GetMapping("/")
    public ResponseEntity<List<Integer>> getUserDevices(@RequestHeader("session-token") String sessionToken) {
        User user;
        try {
            user = Storage.getUserBySessionToken(sessionToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        if (user != null) {
            List<Integer> devices = user.getDevices();
            //TODO return a list, does this work?
            return ResponseEntity.ok(devices);
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * @param roomId id of the room
     * @return a ResponseEntity with the ids of the devices in room
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<List<Integer>> getRoomDevices(@PathVariable Integer roomId) {
        Room room;
        try {
            room = Storage.getRoom(roomId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        if (room != null) {
            List<Integer> devices = room.getDevices();
            //TODO return a list, does this work?
            return ResponseEntity.ok(devices);
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * @param deviceId id of the device
     * @return a ResponseEntity with the data of the requested device (200), not found (404) if no such device exist or
     * 500 in case of a server error
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDevice(@PathVariable Integer deviceId) {
        Device device;
        try {
            device = Storage.getDevice(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        if (device != null) {
            return ResponseEntity.ok(device);
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * @param device data of the device to be created, name, label and icon are required
     * @return a ResponseEntity with the data of the newly created device (203), or
     * 500 in case of a server error
     */
    @PostMapping("/")
    public ResponseEntity<Device> createDevice(@RequestBody SerialisableDevice device) {
        if (device.name == null || device.label == null || device.icon == null) {
            return ResponseEntity.badRequest().build();
        }

        var newDevice = new Device(device.name, device.label, device.icon);

        try {
            Storage.insertDevice(newDevice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(501).build();
        }

        return ResponseEntity.created(new SerialisableDevice(newDevice));


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
        Device changedDevice;
        try {
            changedDevice = Storage.getDevice(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(501).build();
        }


        if (name != null) {
            changedDevice.name = name;
        }
        if (icon != null) {
            changedDevice.icon = icon;
        }

//        try{
//            Storage.insertDevice(changedDeviceDevice);
//        }catch(Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(501).build();
//        }

        return ResponseEntity.status(203).body(new SerialisableDevice(changedDevice));


    }

    /**
     * @param deviceId id  of the device to be deleted
     * @return a ResponseEntity
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Device> modifyDevice(@PathVariable Integer deviceId) {
        Device deletedDevice;
        try {
            deletedDevice = Storage.getDevice(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(501).build();
        }

        if (deletedDevice == null) {
            return ResponseEntity.notFound().build();
        }

        Storage.deleteDevice(deviceId);

        return ResponseEntity.noContent().build();
    }


}
