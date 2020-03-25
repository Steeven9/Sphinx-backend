package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Event;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class VolatileEventStorage implements  EventStorage {
    private static final HashMap<Integer, Event> events = new HashMap<>();
    private static Integer id = 1;

    private Integer generateId(){
        return id++;
    }

    @Override
    public Event get(Integer id) {
        return events.get(id);
    }

    @Override
    public Integer insert(Event c) {
        if(c.setId(generateId())){
            events.put(c.getId(), c);
            return c.getId();
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        events.remove(id);

    }

    @Override
    public boolean update(Event ev) {
        return false;
    }
}
