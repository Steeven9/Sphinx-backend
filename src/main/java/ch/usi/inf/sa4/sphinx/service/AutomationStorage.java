package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Automation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AutomationStorage extends JpaRepository<Automation, Integer> {


    Optional<List<Automation>> findByUserUsername(String username);
    void deleteAllByUserUsername(String username);
}
