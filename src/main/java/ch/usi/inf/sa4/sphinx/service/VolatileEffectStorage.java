package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Effect;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class VolatileEffectStorage implements EffectStorage {
    private static final HashMap<Integer, Effect> effects = new HashMap<>();
    private static Integer id = 1;

    private Integer generateId(){
        return id++;
    }

    @Override
    public Effect get(Integer id) {
        return effects.get(id);
    }

    @Override
    public Integer insert(Effect e) {
        if(e.setId(generateId())){
            effects.put(e.getId(), e);
            return e.getId();
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        effects.remove(id);
    }

    @Override
    public boolean update(Effect e) {
        if(effects.containsKey(e.getId())){
            effects.put(e.getId(), e);
            return true;
        }
        return false;
    }
}
