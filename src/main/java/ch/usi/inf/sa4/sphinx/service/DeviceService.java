package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Device service.
 * It has methods to interact with Device entities.
 * In general it implements a layer of abstraction over the storage.
 * @see Device
 */
@Service
@Transactional
public class DeviceService {

    @Autowired
    DeviceStorage deviceStorage;
    @Autowired
    CouplingStorage couplingStorage;

    /**
     * @param deviceId the Id of the device
     * @return the Device with corresponding deviceId
     */

    public Optional<Device> get(final Integer deviceId){
        return deviceStorage.findById(deviceId);
    }


    /**
     * @param device the device to update
     * @return true if the update succeds else false
     */
    public boolean update(final Device device){
        if(device.getId()== null || !deviceStorage.existsById(device.getId())) return false;

        try {
            deviceStorage.save(device);
        }catch (final DataIntegrityViolationException e){
            return false;
        }
        return true;
    }


    /**
     * Finds a list of devices that switch the Device with the given id.
     * By switching it is meant that they can have any generic effect on it.
     * @param deviceId the id of the device
     * @return the list of ids of Device(s) that switch it
     * @see Device
     */
    public List<Integer> getSwitchedBy(final int deviceId){
        return couplingStorage.findAll().stream().filter(c -> c.getEffects().stream()
                    .anyMatch(effect -> effect.getDeviceId() == deviceId))
                .map(coupling -> coupling.getEvent().getDeviceId()).collect(Collectors.toList());
    }


    /**
     * Finds a list of Device(s) that the Device with the given Id switches.
     * By switching it is meant that they can have any generic effect on it.
     * @param deviceId the id of the device
     * @return the list of ids of Device(s) that it switches
     * @see Device
     */
    public List<Integer> getSwitches(final int deviceId){
        return couplingStorage.findAll().stream()
                .filter(coupling -> coupling.getEvent().getDeviceId() == deviceId)
                .flatMap(coupling -> coupling.getEffects().stream().map(Effect::getDeviceId))
                .collect(Collectors.toList());
    }

}
