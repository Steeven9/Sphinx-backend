package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;


/**
 * Scene repository.
 */
@Repository
public interface SceneStorage extends JpaRepository<Scene, Integer> {
    List<Scene> findByUserUsername(String username);
    Optional<Scene> findByIdAndUserUsername(int id, String username);


}
