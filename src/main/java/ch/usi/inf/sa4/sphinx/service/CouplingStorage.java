package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository for Couplings.
 */
@Repository
public interface CouplingStorage extends JpaRepository<Coupling, Integer> {
    /**
     * Lists all Couplings belonging to a Device with the given ID
     * @param deviceId the deviceId
     * @return the matching Couplings
     * @see ch.usi.inf.sa4.sphinx.model.Device
     */
    List<Coupling> getCouplingByDeviceId(Integer deviceId);
}
