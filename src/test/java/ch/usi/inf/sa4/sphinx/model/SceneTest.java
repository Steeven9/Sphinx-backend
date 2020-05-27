package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneEffectFactory;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SceneTest {

    @Test
    void testSceneType() {
        assertThrows(IllegalArgumentException.class, () -> SceneType.intToType(0));
    }

    @Test
    void testSceneEffectFactory() {
        SceneEffectFactory sef = new SceneEffectFactory();
        List<Device> list = new ArrayList<>();
        assertThrows(NullPointerException.class, () -> sef.make(list, "name", null, new Object()));
    }

    @Test
    void coverScene() {
        User user = new User();
        Scene scene = new Scene(user, null, null);
        scene.setUser(user);
    }
}