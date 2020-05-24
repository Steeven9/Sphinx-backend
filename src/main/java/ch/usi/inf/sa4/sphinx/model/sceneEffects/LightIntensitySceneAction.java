package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.DimmableLight;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class LightIntensitySceneAction extends SceneAction<DimmableLight> {
    private double intensity;

    public LightIntensitySceneAction(List<DimmableLight> lights, double intensity, String name) {
        super( lights, name);
        this.intensity = intensity;
    }

    public LightIntensitySceneAction() {
    }


    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),
                getType().toInt(),
                getName(),
                intensity,
                null,
                getDevices().stream().map(Device::getId).collect(Collectors.toList()));
    }

    @Override
    public SceneType getType() {
        return SceneType.LIGHT_INTENSITY;
    }

    @Override
    public void run() {
        List<DimmableLight> devices = getDevices();
        devices.forEach(device -> {
            device.setOn(true);
            device.setState(intensity);
            ServiceProvider.getDeviceService().update(device);
        });
    }
}
