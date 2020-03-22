package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.beans.factory.annotation.Autowired;

public final class DeviceService {

    @Autowired
    DeviceStorage deviceStorage;

    /**
     * @param deviceId the Id of the device
     * @return the Device with corresponding deviceId
     */
    public Device get(String deviceId){
        return deviceStorage.get(deviceId);
    }
}
