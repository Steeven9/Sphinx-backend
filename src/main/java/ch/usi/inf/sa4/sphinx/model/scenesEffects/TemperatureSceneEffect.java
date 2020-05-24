package ch.usi.inf.sa4.sphinx.model.scenesEffects;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Thermostat;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;

import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class TemperatureSceneEffect extends SceneEffect<Thermostat> {
    private boolean power;
    private double temperature;

    public TemperatureSceneEffect(List<Thermostat> thermos ,String name, boolean power, double  temperature) {
        super(thermos, name);
        this.power = power;
        this.temperature = temperature;
    }
    @Override
    public SerialisableSceneEffect serialise() {
        return new SerialisableSceneEffect(getId(),getType().toInt(), getName(), temperature,power,getDevices().stream().map(Device::getId).collect(Collectors.toList()));
    }

    @Override
    public SceneTypes getType() {
        return SceneTypes.TEMPERATURE;
    }
    public TemperatureSceneEffect() {
    }

    @Override
    public void run() {
        List<Thermostat> devices = getDevices();
        devices.forEach(device -> {
            device.setOn(power);
            device.setTargetTemp(temperature);
        });
    }
}
