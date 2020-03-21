package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;


@Repository("volatileDeviceStorage")
public class VolatileDeviceStorage implements DeviceStorage {
    private static final HashMap<String, Device> devices = new HashMap<>();


    @Override
    public Device get(String deviceId) {
        return devices.get(deviceId);
    }

    @Override
    public String insert(Device device) {
        Device savedDevice = device.makeCopy();
        savedDevice.setId(UUID.randomUUID().toString());
        devices.put(savedDevice);
        return savedDevice.getId();
    }

    @Override
    public void delete(String deviceId) {
        devices.remove(deviceId);
    }

    @Override
    public boolean updateDevice(Device updatedDevice) {
        if(devices.get(updatedDevice.getId()) == null){
            return false;
        }
        devices.put(updatedDevice.getId(), updatedDevice.makeCopy());

        return true;
    }
}
