package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SceneService {
    @Autowired
    SceneStorage sceneStorage;

    public Optional<Scene> get(Integer sceneId) {
        return sceneStorage.findById(sceneId);
    }
}
