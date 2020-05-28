package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneAction;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneEffectFactory;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SceneService {
    @Autowired
    SceneStorage sceneStorage;
    @Autowired
    private UserService userService;
    @Qualifier("deviceStorage")
    @Autowired
    private DeviceStorage deviceStorage;


    public Optional<Scene> get(Integer sceneId) {
        return sceneStorage.findById(sceneId);
    }


    public List<Scene> getOwnedScenes(String username) {
        return sceneStorage.findByUserUsername(username);
    }

    public Optional<Scene> createScene(String username, String sceneName, String icon) {
        User user = userService.get(username).orElseThrow(() -> new NotFoundException(""));
        Scene scene = new Scene(user, sceneName, icon);
        return Optional.of(sceneStorage.save(scene));
    }

    public void addEffect(int sceneId, List<Integer> deviceIds, SceneType type, String effectName, Object target) {
        Scene scene = sceneStorage.findById(sceneId).orElseThrow(() -> new NotFoundException(""));
        List<Device> devices = deviceStorage.findAllById(deviceIds);
        SceneAction effect = SceneEffectFactory.make(devices, effectName, type, target);
        scene.addEffect(effect);
        sceneStorage.save(scene);
    }

    public void removeEffects(int sceneId) {
        Scene scene = sceneStorage.findById(sceneId).orElseThrow(() -> new NotFoundException(""));
        scene.clearEffects();
        sceneStorage.save(scene);
    }

    public boolean isOwnedBy(String username, int sceneId) {
        return sceneStorage.findByIdAndUserUsername(sceneId, username).isPresent();
    }

    public void deleteScene(int scenId){
        sceneStorage.deleteById(scenId);
    }
}
