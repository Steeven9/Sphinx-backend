package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling.Coupling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for Couplings.
 */
@Repository
public interface CouplingStorage extends JpaRepository<Coupling, Integer> {
//    /**
//     * Lists all Couplings belonging to a Device with the given ID
//     * @param deviceId the deviceId
//     * @return the matching Couplings
//     * @see ch.usi.inf.sa4.sphinx.model.Device
//     */
//    List<Coupling> getCouplingByDeviceId(Integer deviceId);

    Optional<Coupling> findByDeviceIdAndDevice2Id(Integer id1, Integer id2);


    List<Coupling> findByDeviceIdOrderById(Integer id);

    List<Coupling> findByDevice2IdOrderById(Integer id);

    void deleteByDeviceIdAndDevice2Id(Integer id1, Integer id2);
}
