package ch.usi.inf.sa4.sphinx.service;
import ch.usi.inf.sa4.sphinx.model.Event;

public interface EventStorage {
    /** Getter for events in storage.
     * @param id the id of the event to get
     * @return the id of the event
     **/
    Event get(Integer id);

    /** Insert event in storage.
     * @param c the event to be inserted
     * @return the id of the event inserted
     **/
    Integer insert(Event c);

    /** Delete event from storage.
     * @param id the id of the event to be deleted
     **/
    void delete(Integer id);

    /** Update event in storage.
     * @param ev the event to be updated
     * @return true if successfully updated, false otherwise
     **/
    boolean update(Event ev);
}
