package ch.usi.inf.sa4.sphinx.service;
import ch.usi.inf.sa4.sphinx.model.Event;

public interface EventStorage {
    Event get(int id);

    Integer insert(Event c);

    void delete(int id);

    boolean update(Event ev);
}
