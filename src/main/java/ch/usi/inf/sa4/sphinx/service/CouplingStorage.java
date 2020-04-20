package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CouplingStorage extends JpaRepository<Coupling, Integer> {
    List<Coupling> getCouplingByDeviceId(Integer deviceId);
}
