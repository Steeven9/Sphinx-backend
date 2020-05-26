package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling.Coupling;
import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Device repository.
 */
@Repository
public interface DeviceStorage extends JpaRepository<Device, Integer> {
}


