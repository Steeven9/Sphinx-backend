package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.springframework.stereotype.Repository;


public interface CouplingStorage {
    /** Getter for the id.
     * @return id
     **/
    Coupling get(int id);

    /** Insert Coupling.
     * @return id of coupling inserted
     **/
    Integer insert(Coupling c);

    /** Delete coupling based on its id.
     * @param id the id of the coupling to delete
     **/
    void delete(int id);


    /** Update coupling.
     * @param updatedDevice coupling to be updated
     * @return true if successfully updated, false otherwise
     **/
    boolean update(Coupling updatedDevice);

}
