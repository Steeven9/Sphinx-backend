package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import ch.usi.inf.sa4.sphinx.model.Effect;
import ch.usi.inf.sa4.sphinx.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouplingService {

    @Autowired
    private Storage<Integer, Coupling> couplingStorage;
    @Autowired
    private Storage<Integer, Effect<?>> effectStorage;
    @Autowired
    private VolatileEventStorage eventStorage;


    /**
     * @param id id of the coupling to get
     * @return the coupling with the matching id
     */
    public Coupling get(int id) {
        return couplingStorage.get(id);
    }

    /**
     * @param id id of the Coupling that owns the effects
     * @return the effects belonging to the specified coupling
     */
    public List<Effect> getEffects(@NotNull Integer id) {
        Coupling c = couplingStorage.get(id);
        if (c == null) return null;
        return c.getEffectIds().stream().map(effectStorage::get).collect(Collectors.toList());
    }


    /**
     * @param id id of the Effect
     * @return the stored Effect with the given Id
     */
    public Effect<?> getEffect(@NotNull Integer id){
      return effectStorage.get(id);
    }


    /**
     * @param id id of the EVent
     * @return the stored Event with the given Id
     */
    public Event getEvent(@NotNull Integer id) {
        return eventStorage.get(id);
    }


    /**
     * @param event Event to add to the new coupling
     * @param effect Effect to add to the new coupling
     * @return The id of the newly stored Coupling
     */
    //TODO make safe, check the username?
    public Integer addCoupling(Event<Double> event, Effect<Double> effect) {
        Integer storedEffect = effectStorage.insert(effect);
        Integer storedEvent = eventStorage.insert(event);
        Coupling newCoupling;

        if (storedEffect != null && storedEvent != null) {
            newCoupling = new Coupling(storedEvent, storedEffect);
            newCoupling.setId(event.device);
            return couplingStorage.insert(newCoupling);
        }

        effectStorage.delete(storedEffect);
        eventStorage.delete(storedEvent);
        return null;
    }


    /**
     * Deletes a given Coupling from the storage
     * @param id id of the coupling to delete
     */
    public void delete(int id) {
        couplingStorage.delete(id);
    }

    /**
     * adds an effect to the Coupling in storagee with a given id
     * @param id id of the Coupling
     * @param effect effect to add
     * @return true if succes else false
     */
    public boolean addEffect(int id, Integer effect) {
        Coupling c = get(id);
        if (c == null) {
            return false;
        }

        c.addEffect(effect);
        return couplingStorage.update(c);
    }
}
