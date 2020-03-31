package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Event;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class VolatileEventStorage implements  EventStorage {
    private static final HashMap<Integer, Event> events = new HashMap<>();
    private static Integer id = 1;


    /** Id generator.
     * @return id
     **/
    private Integer generateId(){
        return id++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event get(Integer id) {
        return events.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insert(Event c) {
        if(c.setId(generateId())){
            events.put(c.getId(), c);
            return c.getId();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer id) {
        events.remove(id);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Event ev) {
        return false;
    }
}
