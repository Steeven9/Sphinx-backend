package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import ch.usi.inf.sa4.sphinx.model.Effect;
import ch.usi.inf.sa4.sphinx.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouplingService {

    @Autowired
    private CouplingStorage couplingStorage;
    @Autowired
    private EffectStorage effectStorage;
    @Autowired
    private VolatileEventStorage eventStorage;


    /** Getter for couplings in storage.
     * @param id the id of the coupling to get
     * @return an instance of Coupling
     **/
    public Coupling get(int id) {
        return couplingStorage.get(id);
    }

    /** Getter for effects in storage.
     * @param id the id of the effect to get
     * @return an instance of Effect
     **/
    public Effect getEffect(Integer id){
        return effectStorage.get(id);
    }

    /** Getter for events in storage.
     * @param id the id of the event to get
     * @return an instance of Event
     **/
    public Event getEvent(Integer id){
        return eventStorage.get(id);
    }


    //TODO make safe, check the username?
    /** Add coupling to storage.
     * @param event the event in the coupling to be added
     * @param effect the effect in the coupling to be added
     * @return the id of the new coupling
     **/
    public Integer addCoupling (Event<Double> event, Effect<Double> effect) {
        Integer storedEffect = effectStorage.insert(effect);
        Integer storedEvent = eventStorage.insert(event);
        Coupling newCoupling;

        if(storedEffect != null && storedEvent != null){
            newCoupling = new Coupling(storedEvent, storedEffect);
            newCoupling.setId(event.device);
            return couplingStorage.insert(newCoupling);
        }

        effectStorage.delete(storedEffect);
        eventStorage.delete(storedEvent);
        return null;
    }


    /** Delete coupling in storage.
     * @param id the id of the coupling to be deleted
     **/
    public void delete(int id) {
        couplingStorage.delete(id);
    }

    /** Add effect to coupling.
     * @param id the id of the coupling where we want to add an effect
     * @param effect the id of the effect we want to add
     * @return true if successfully updated, false otherwise
     **/
    public boolean addEffect(int id, Integer effect){
        Coupling c = get(id);
        if (c == null) {
            return false;
        }

        c.addEffect(effect);
        return  couplingStorage.update(c);
    }
}
