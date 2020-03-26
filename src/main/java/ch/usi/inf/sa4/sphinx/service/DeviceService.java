package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Service
public final class DeviceService {

    @Autowired
    DeviceStorage deviceStorage;

    /**
     * @param deviceId the Id of the device
     * @return the Device with corresponding deviceId
     */
    public Device get(Integer deviceId){
        return deviceStorage.get(deviceId);
    }


    /**
     * @param device the device to update
     * @return true if the update succeds else false
     */
    public boolean update(Device device){
        return deviceStorage.update(device);
    }
}
