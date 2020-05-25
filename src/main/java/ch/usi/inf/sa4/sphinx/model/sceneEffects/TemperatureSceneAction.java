package ch.usi.inf.sa4.sphinx.model.sceneEffects;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Thermostat;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
public class TemperatureSceneAction extends SceneAction<Thermostat> {
    private double temperature;

    public TemperatureSceneAction(List<Thermostat> thermos, double temperature, String name) {
        super(thermos, name);
        this.temperature = temperature;
    }

    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),
                getType().toInt(),
                getName(),
                temperature,
                null,
                getDevices().stream().map(Device::getId).collect(Collectors.toList())
        );
    }

    @Override
    public SceneType getType() {
        return SceneType.TEMPERATURE;
    }

    @Override
    public void run() {
        List<Thermostat> devices = getDevices();
        devices.forEach(device -> {
            device.setOn(true);
            device.setTargetTemp(temperature);
            ServiceProvider.getDeviceService().update(device);
        });
    }
}
