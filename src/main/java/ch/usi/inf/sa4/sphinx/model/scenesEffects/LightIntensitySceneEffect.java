package ch.usi.inf.sa4.sphinx.model.scenesEffects;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.DimmableLight;
import ch.usi.inf.sa4.sphinx.model.StorableE;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class LightIntensitySceneEffect extends SceneEffect<DimmableLight>{
    private boolean power;
    private double intensity;

    public LightIntensitySceneEffect(List<DimmableLight> lights, boolean power, double intensity, String name) {
        super(lights, name);
        this.power = power;
        this.intensity = intensity;
    }

    public LightIntensitySceneEffect() {
    }


    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),getType().toInt(), getName(), intensity,power,getDevices().stream().map(Device::getId).collect(Collectors.toList()));
    }

    @Override
    public SceneTypes getType() {
        return SceneTypes.LIGHT_INTENSITY;
    }

    @Override
    public void run() {
        List<DimmableLight> devices = getDevices();
        devices.forEach(device -> {
            device.setOn(power);
            device.setState(intensity);
        });
    }
}
