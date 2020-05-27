package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Automation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface AutomationStorage extends JpaRepository<Automation, Integer> {

    @Transactional
    Optional<List<Automation>> findByUserUsername(String username);
}
