package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Coupling service..
 * It has methods to interact with Coupling entities.
 * In general it implements a layer of abstraction over the storage.
 * @see Coupling
 */
@Service
public class CouplingService {

    @Autowired
    private CouplingStorage couplingStorage;


    /**
     * Getter for couplings in storage.
     *
     * @param id the id of the coupling to get
     * @return an instance of Coupling
     **/
    public Coupling get(final Integer id) {
        return couplingStorage.findById(id).orElse(null);
    }


    /**
     * @param id id of the Coupling that owns the effects
     * @return the effects belonging to the specified coupling
     */
    public List<Effect> getEffects(@NotNull final Integer id) {
        return couplingStorage.findById(id).map(Coupling::getEffects).orElseGet(ArrayList::new);
    }

    /**
     * @param id1 first id of the device couplings that needs to be deleted
     * @param id2 second id of the device couplings that needs to be deleted
     */
    public void removeByDevicesIds(final Integer id1, final Integer id2){
        final List<Coupling> couplings = couplingStorage.findAll();
        for (final Coupling coupling : couplings) {
            final List<Effect> effects = coupling.getEffects();

            effects.removeIf(effect -> effect.getDeviceId().equals(id1) && coupling.getEvent().getDeviceId() == id2
                                    || effect.getDeviceId().equals(id2) && coupling.getEvent().getDeviceId() == id1);

            if(effects.size() > 1){
                couplingStorage.save(coupling);
            }else{
                //deleting the coupling
                couplingStorage.delete(coupling);
            }
        }
    }

    /**
     * Add coupling to storage.
     *
     * @param event  the event in the coupling to be added
     * @param effects the effect in the coupling to be added
     * @return the id of the new coupling
     *
     * @param <T> parametrized type of the Event and Effect*/
    public <T> Integer addCoupling(final Event<T> event, final List<? extends Effect<T>> effects) {
            final Coupling newCoupling = new Coupling(event, effects);
            return couplingStorage.save(newCoupling).getId();
    }


    /**
     * Works like {@link CouplingService#addCoupling(Event, List)} but with a single effect.
     * @param event  the event in the coupling to be added
     * @param effect the effect in the coupling to be added
     * @return the id of the new coupling
     *
     * @param <T> parametrized type of the Event and Effect
     * */
    public <T> Integer addCoupling(final Event<T> event, final Effect<T> effect) {
        return addCoupling(event, List.of(effect));
    }


    /**
     * Delete coupling in storage.
     *
     * @param id the id of the coupling to be deleted
     **/
    public void delete(final Integer id) {
        couplingStorage.deleteById(id);
    }

    /**
     * Add effect to coupling.
     *
     * @param id     the id of the coupling where we want to add an effect
     * @param effect the id of the effect we want to add
     * @return true if successfully updated, false otherwise
     **/
    public boolean addEffect(final Integer id, @NotNull final Effect effect) {
        return couplingStorage.findById(id).map(coupling -> {
                    try {
                        coupling.addEffect(effect);
                        couplingStorage.save(coupling);
                        return Boolean.TRUE;
                    } catch (IllegalArgumentException e) {
                        return Boolean.FALSE;
                    }
                }
        ).orElse(Boolean.FALSE);
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
        final DeviceType type1 = device1.getDeviceType();
        final DeviceType type2 = device2.getDeviceType();
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
            this.addCoupling(eventHelper(type1, device1.getId()), effectHelper(type1, type2, device2.getId()));
        } else {
            this.addCoupling(eventHelper(type2, device2.getId()), effectHelper(type2, type1, device1.getId()));
        }
        return true;
    }
}
