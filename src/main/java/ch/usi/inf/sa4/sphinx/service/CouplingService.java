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


    public Coupling get(int id) {
        return couplingStorage.get(id);
    }

    public Effect getEffect(Integer id){
        return effectStorage.get(id);
    }

    public Event getEvent(Integer id){
        return eventStorage.get(id);
    }


    //TODO make safe, check the username?
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


    public void delete(int id) {
        couplingStorage.delete(id);
    }

    public boolean addEffect(int id, Integer effect){
        Coupling c = get(id);
        if (c == null) {
            return false;
        }

        c.addEffect(effect);
        return  couplingStorage.update(c);
    }
}
