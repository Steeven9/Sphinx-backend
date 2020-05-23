package ch.usi.inf.sa4.sphinx.model.scenesEffects;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.DimmableLight;
import ch.usi.inf.sa4.sphinx.model.SmartCurtain;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class CurtainsApertureSceneEffect extends SceneEffect<SmartCurtain>{
    private boolean power;
    private double aperture;

    public CurtainsApertureSceneEffect(List<SmartCurtain> curtains,String name, boolean power, double aperture) {
        super(curtains, name);
        this.power = power;
        this.aperture = aperture;
    }
    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),getType().toInt(), getName(), aperture,power,getDevices().stream().map(Device::getId).collect(Collectors.toList()));
    }

    @Override
    public SceneTypes getType() {
        return SceneTypes.CURTAINS_APERTURE;
    }
    public CurtainsApertureSceneEffect() {
    }

    @Override
    public void run() {
        List<SmartCurtain> devices = getDevices();
        devices.forEach(device -> {
            device.setOn(power);
            device.setState(aperture);
        });
    }
}
