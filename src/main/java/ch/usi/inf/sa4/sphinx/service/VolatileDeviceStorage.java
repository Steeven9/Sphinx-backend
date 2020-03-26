package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;


//METHOD ACCESS SHOULD BE DEFAULT NOT PUBLIC BUT IT CANT BE DONE SINCE IT IMPLEMENTS AN INTERFACE, MIGHT MAKE STORAGE
//AN ABSTRACT CLASS TO SET DIFFERENT PRIVACY.



@Repository("volatileDeviceStorage")
public class VolatileDeviceStorage implements DeviceStorage {
    private static final HashMap<Integer, Device> devices = new HashMap<>();
    private static Integer id = 1;

    private Integer generateId(){
        return id++;
    }


    @Override
    public Device get(Integer deviceId) {
        Device storageDevice = devices.get(deviceId);
        if(storageDevice == null){
            return null;
        }
        Device returnDevice = storageDevice.makeCopy();
        //The copy is without ID
        returnDevice.setId(storageDevice.getId());

        return devices.get(deviceId);
    }

    @Override
    public Integer insert(Device device) {
        Device savedDevice = device.makeCopy();
        Integer newId = generateId();
        if(savedDevice.setId(newId)) {
            devices.put(newId, savedDevice);
            return newId;
        }
        return null;

    }

    @Override
    public void delete(Integer deviceId) {
        devices.remove(deviceId);
    }

    @Override
    public boolean update(Device updatedDevice) {
        if(devices.get(updatedDevice.getId()) == null || !devices.containsKey(updatedDevice.getId())){
            return false;
        }
        devices.put(updatedDevice.getId(), updatedDevice.makeCopy());
        return true;
    }
}
