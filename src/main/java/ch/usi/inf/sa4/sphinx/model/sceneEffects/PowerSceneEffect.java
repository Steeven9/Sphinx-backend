package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class PowerSceneEffect extends SceneEffect<Device> {
    private boolean power;

    public PowerSceneEffect(List<Device> devices, boolean power, String name) {
        super( devices, name);
        this.power = power;
    }

    public PowerSceneEffect() {
    }

    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),
                getType().toInt(),
                getName(),
                null,
                power,
                getDevices().stream().map(Device::getId).collect(Collectors.toList()));
    }

    @Override
    public SceneType getType() {
        return SceneType.POWER;
    }


    @Override
    public void run() {
        getDevices().forEach(device -> {
            device.setOn(power);
            ServiceProvider.getDeviceService().update(device);
        });
    }


}
