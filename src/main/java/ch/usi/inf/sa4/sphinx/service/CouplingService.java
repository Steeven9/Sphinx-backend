package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.model.Coupling;
import ch.usi.inf.sa4.sphinx.model.Effect;
import ch.usi.inf.sa4.sphinx.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public Coupling get(Integer id) {
        return couplingStorage.findById(id).orElse(null);
    }


    /**
     * @param id id of the Coupling that owns the effects
     * @return the effects belonging to the specified coupling
     */
    public List<Effect> getEffects(@NotNull Integer id) {
        return couplingStorage.findById(id).map(Coupling::getEffects).orElse(new ArrayList<>());
    }

    /**
     * @param id1 first id of the device couplings that needs to be deleted
     * @param id2 second id of the device couplings that needs to be deleted
     */
    public void removeByDevicesIds(Integer id1, Integer id2){
        List<Coupling> c = couplingStorage.findAll();
        for (Coupling coupling : c) {
            List<Effect> e = coupling.getEffects();

            //if effect has either device id1 or id2 in it, we delete that effect
            e.removeIf(effect -> effect.getDeviceId().equals(id1) || effect.getDeviceId().equals(id2));

            //deleting the coupling, if didn't need to delete it will be added later
            couplingStorage.delete(coupling);

            //if the coupling had not been necessary to remove, we create a new coupling with the new resulting event and effects (that had been reduced before)
            if(e.size() > 1){
                addCoupling(coupling.getEvent(), new ArrayList<>(coupling.getEffects()));
            }
        }
    }

    /**
     * Add coupling to storage.
     *
     * @param event  the event in the coupling to be added
     * @param effects the effect in the coupling to be added
     * @return the id of the new coupling
     **/
    public <T> Integer addCoupling(Event<T> event, List<Effect<T>> effects) {
            Coupling newCoupling = new Coupling(event, effects);
            return couplingStorage.save(newCoupling).getId();
    }


    public <T> Integer addCoupling(Event<T> event, Effect<T> effect) {
        return addCoupling(event, List.of(effect));
    }


    /**
     * Delete coupling in storage.
     *
     * @param id the id of the coupling to be deleted
     **/
    public void delete(Integer id) {
        couplingStorage.deleteById(id);
    }

    /**
     * Add effect to coupling.
     *
     * @param id     the id of the coupling where we want to add an effect
     * @param effect the id of the effect we want to add
     * @return true if successfully updated, false otherwise
     **/
    public boolean addEffect(Integer id, @NotNull Effect effect) {
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
