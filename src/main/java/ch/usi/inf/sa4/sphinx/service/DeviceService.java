package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    CouplingService couplingService;
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


    /**
     * Generates an Event basing on switch type.
     *
     * @param type the DeviceType of a switch
     * @param key  the id of the switch
     * @return event corresponding to the switch
     */
    private <T> Event<T> eventHelper(final DeviceType type, final Integer key) {
        if (type == DeviceType.SWITCH) {
            return (Event<T>) new SwitchChangedEvent(key);
        } else if (type == DeviceType.STATELESS_DIMMABLE_SWITCH) {
            return (Event<T>) new StatelessDimmSwitchChangedEvent(key, 0.1);
        } else if (type == DeviceType.DIMMABLE_SWITCH) {
            return (Event<T>) new DimmSwitchChangedEvent(key);
        } else {
            return null;
        }
    }

    /**
     * Generates an Effect basing on switch type and light type.
     *
     * @param switchType the DeviceType of a switch
     * @param lightType  the DeviceType of a light
     * @param key        the id of the switch
     * @return corresponding effect
     */
    private <T> Effect<T> effectHelper(final DeviceType switchType, final DeviceType lightType, final Integer key) {
        if (switchType == DeviceType.SWITCH) {
            return (Effect<T>) (new DeviceSetOnEffect(key));
        } else if (lightType == DeviceType.DIMMABLE_LIGHT) {
            if (switchType == DeviceType.STATELESS_DIMMABLE_SWITCH) {
                return (Effect<T>) (new DimmableLightStateInc(key));
            } else if (switchType == DeviceType.DIMMABLE_SWITCH) {
                return (Effect<T>) (new DimmableLightStateSet(key));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Creates a coupling between a light and a switch.
     *
     * @param device1 first device to couple
     * @param device2 second device to couple
     * @return true if coupling was succeed, false otherwise
     */
    public boolean createCoupling(final Device device1, final Device device2) {
        final List<DeviceType> switches = List.of(DeviceType.SWITCH, DeviceType.DIMMABLE_SWITCH, DeviceType.STATELESS_DIMMABLE_SWITCH);
        final List<DeviceType> lights = List.of(DeviceType.LIGHT, DeviceType.DIMMABLE_LIGHT);
        final DeviceType type1 = DeviceType.deviceToDeviceType(device1);
        final DeviceType type2 = DeviceType.deviceToDeviceType(device2);
        final boolean ordered;

        //check if first is a switch and second is a light
        if (switches.contains(type1) && lights.contains(type2)) {
            ordered = true;
        } else if (switches.contains(type2) && lights.contains(type1)) {
            ordered = false;
        } else {
            return false;
        }

        if (ordered) {
            couplingService.addCoupling(eventHelper(type1, device1.getId()), effectHelper(type1, type2, device2.getId()));
        } else {
            couplingService.addCoupling(eventHelper(type2, device2.getId()), effectHelper(type2, type1, device1.getId()));
        }
        return true;
    }
}
