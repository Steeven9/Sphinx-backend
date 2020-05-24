package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.DimmableLight;
import ch.usi.inf.sa4.sphinx.model.SmartCurtain;
import ch.usi.inf.sa4.sphinx.model.Thermostat;

import java.util.ArrayList;
import java.util.List;


public final class SceneEffectFactory {
    private SceneEffectFactory() {
    }

    public static SceneAction make(List<Device> devices, String name, SceneType type, Object target) {
        try {
            switch (type) {
                case POWER:
                    return new PowerSceneAction(devices, (boolean) target, name);
                case TEMPERATURE:
                    List<Thermostat> tmp = new ArrayList<>();
                    devices.forEach(device -> tmp.add((Thermostat) device));
                    return new TemperatureSceneAction(tmp, (double) target, name);
                case LIGHT_INTENSITY:
                    List<DimmableLight> tmp2 = new ArrayList<>();
                    devices.forEach(device -> tmp2.add((DimmableLight) device));
                    return new LightIntensitySceneAction(tmp2, (double) target, name);
                case CURTAINS_APERTURE:
                    List<SmartCurtain> tmp3 = new ArrayList<>();
                    devices.forEach(device -> tmp3.add((SmartCurtain) device));
                    return new CurtainsApertureSceneAction(tmp3, (double) target, name);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("");
        }
        throw new IllegalArgumentException("");
    }
}
