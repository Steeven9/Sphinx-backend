package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.springframework.stereotype.Repository;


public interface CouplingStorage {
    Coupling get(int id);

    Integer insert(Coupling c);

    void delete(int id);


    boolean update(Coupling updatedDevice);

}
