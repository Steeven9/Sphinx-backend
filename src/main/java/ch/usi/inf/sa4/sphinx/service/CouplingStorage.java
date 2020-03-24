package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.springframework.stereotype.Repository;


public interface CouplingStorage {

    /**
     * @param id identifier for a Coupling
     * @return the Coupling identified by id if in the Storage else null
     */
    Coupling get(int id);

    /**
     * @param c  the coupling to insert in the Storage
     * @return the id of the inserted coupling
     */
    Integer insert(Coupling c);

    /**
     * @param id id of the coupling to delete
     */
    void delete(int id);






}
