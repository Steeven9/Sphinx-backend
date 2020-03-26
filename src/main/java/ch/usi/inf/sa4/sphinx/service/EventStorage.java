package ch.usi.inf.sa4.sphinx.service;
import ch.usi.inf.sa4.sphinx.model.Event;

public interface EventStorage {
    Event get(Integer id);

    Integer insert(Event c);

    void delete(Integer id);

    boolean update(Event ev);
}
