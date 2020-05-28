package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.triggers.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriggerStorage extends JpaRepository<Trigger, Integer> {
    List<Trigger> findByAutomationId(int automationId);
    void deleteByAutomationId(int automationId);

}
