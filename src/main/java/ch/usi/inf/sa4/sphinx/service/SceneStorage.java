package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Scene;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SceneStorage extends JpaRepository<Scene, Integer> {
}
