package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public final class DeviceService {

    @Autowired
    CouplingService couplingService;
    @Autowired
    DeviceStorage deviceStorage;

    /**
     * @param deviceId the Id of the device
     * @return the Device with corresponding deviceId
     */

    public Device get(Integer deviceId){
        return deviceStorage.findById(deviceId).orElse(null);
    }


    /**
     * @param device the device to update
     * @return true if the update succeds else false
     */
    public boolean update(Device device){
        deviceStorage.save(device);
        return true;
    }


    /**
     * Generates an Event basing on switch type.
     *
     * @param type the DeviceType of a switch
     * @param key  the id of the switch
     * @return event corresponding to the switch
     */
    private <T> Event<T> eventHelper(DeviceType type, Integer key) {
        if (DeviceType.SWITCH.equals(type)) {
            return (Event<T>) new SwitchChangedEvent(key, this);
        } else if (DeviceType.STATELESS_DIMMABLE_SWITCH.equals(type)) {
            return (Event<T>) new StatelessDimmSwitchChangedEvent(key, 0.1, this);
        } else if (DeviceType.DIMMABLE_SWITCH.equals(type)) {
            return (Event<T>) new DimmSwitchChangedEvent(key, this);
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
    private <T> Effect<T> effectHelper(DeviceType switchType, DeviceType lightType, Integer key) {
        if (DeviceType.SWITCH.equals(switchType)) {
            return (Effect<T>) (new DeviceSetOnEffect(key));
        } else if (DeviceType.DIMMABLE_LIGHT.equals(lightType)) {
            if (DeviceType.STATELESS_DIMMABLE_SWITCH.equals(switchType)) {
                return (Effect<T>) (new DimmableLightStateInc(key));
            } else if (DeviceType.DIMMABLE_SWITCH.equals(switchType)) {
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
    public boolean createCoupling(Device device1, Device device2) {
        List<DeviceType> switches = List.of(DeviceType.SWITCH, DeviceType.DIMMABLE_SWITCH, DeviceType.STATELESS_DIMMABLE_SWITCH);
        List<DeviceType> lights = List.of(DeviceType.LIGHT, DeviceType.DIMMABLE_LIGHT);
        DeviceType type1 = DeviceType.deviceToDeviceType(device1);
        DeviceType type2 = DeviceType.deviceToDeviceType(device2);
        boolean ordered;

        //check if first is a switch and second is a light
        if (switches.contains(type1) && lights.contains(type2)) {
            ordered = true;
        } else if (switches.contains(type2) && lights.contains(type1)) {
            ordered = false;
        } else {
            return false;
        }

        if (ordered) {
            couplingService.addCoupling(eventHelper(type1, device1.getKey()), effectHelper(type1, type2, device2.getKey()));
        } else {
            couplingService.addCoupling(eventHelper(type2, device2.getKey()), effectHelper(type2, type1, device1.getKey()));
        }
        return true;
    }
}
