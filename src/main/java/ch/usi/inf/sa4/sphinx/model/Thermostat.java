package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

import java.text.DecimalFormat;

/**
 * A Thermostat is a device, which can control the temperature in a given room. It is embedded to a Temperature sensor.
 */
public class Thermostat extends TempSensor {
    private double targetTemp;
    private double averageTemp;
    private int state;

    /**
     * Constructor.
     * Initialize a thermostat, which is on and has idle as internal state. Its initial is set to its actual value.
     */
    public Thermostat() {
        super();
        this.targetTemp = this.getValue();
        this.state = 1; //idle
        this.averageTemp = this.targetTemp;
    }

    /*public Thermostat(Thermostat d) {
        super(d);
        this.targetTemp = d.targetTemp;
        this.state = d.state;
        this.averageTemp = d.averageTemp;
    }*/

    public double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    /**
     * Sets a target temperature to this thermostat and changes the internal state.
     *
     * @param target the target temperature
     */
    public void setTargetTemp(double target) {
        if (target > this.getValue()) {
            this.state = 3; //heating
        } else if (target < this.getValue()) {
            this.state = 2; //cooling
        }
        this.targetTemp = target;
    }

    /**
     * Turns off the thermostat.
     */
    public void turnOff() {
        this.on = false;
        this.state = 0; //off
    }

    /**
     * Turns on the thermostat.
     */
    public void turnOn() {
        this.on = true;
        this.state = 1; //idle
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return new DecimalFormat("#.##").format(this.getValue()) + " " + this.getPhQuantity() + " Avg: " + this.getAverageTemp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.THERMOSTAT;
    }
}
