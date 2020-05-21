package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.conditions.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionStorage extends JpaRepository<Condition,Integer > {

}
