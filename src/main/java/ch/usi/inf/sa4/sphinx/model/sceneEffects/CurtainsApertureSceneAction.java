package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.SmartCurtain;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class CurtainsApertureSceneAction extends SceneAction<SmartCurtain> {
    private double aperture;

    public CurtainsApertureSceneAction() {
    }
    public CurtainsApertureSceneAction(List<SmartCurtain> curtains, double aperture, String name) {
        super( curtains, name);
        this.aperture = aperture;
    }

    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),
                getType().toInt(),
                getName(),
                aperture,
                null,
                getDevices().stream().map(Device::getId).collect(Collectors.toList()));
    }
    @Override
    public SceneType getType() {
        return SceneType.CURTAINS_APERTURE;
    }

    @Override
    public void run() {
        List<SmartCurtain> devices = getDevices();
        devices.forEach(device -> {
            device.setOn(true);
            device.setState(aperture);
            ServiceProvider.getDeviceService().update(device);
        });
    }
}
