package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import ch.usi.inf.sa4.sphinx.model.Effect;
import ch.usi.inf.sa4.sphinx.model.Event;
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
    @Autowired
    private DeviceStorage deviceStorage;


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
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                }
        ).orElse(false);
    }
}
