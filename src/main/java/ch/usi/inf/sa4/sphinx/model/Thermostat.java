package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

import java.text.DecimalFormat;

/**
 * A Thermostat is a device, which can control the temperature in a given room. It is embedded to a Temperature sensor.
 */
public class Thermostat extends TempSensor {

    private double targetTemp;
    private double averageTemp;
    private States state;

    /**
     * Constructor.
     * Initialize a thermostat, which is on and has idle as internal state. Its initial is set to its actual value.
     */
    public Thermostat(RoomService roomService, CouplingService couplingService) {
        super(roomService, couplingService);
        this.targetTemp = this.getValue();
        this.state = States.IDLE;
        this.averageTemp = this.targetTemp;
    }

    /**
     * Sets a target temperature to this thermostat and changes the internal state.
     *
     * @param target the target temperature
     */
    public void setTargetTemp(double target) {
        if (target > this.getValue()) {
            this.state = States.HEATING;
        } else if (target < this.getValue()) {
            this.state = States.COOLING;
        }
        this.targetTemp = target;
    }

    public Thermostat(Thermostat d) {
        super(d);
        this.targetTemp = d.targetTemp;
        this.state = d.state;
        this.averageTemp = d.averageTemp;
    }

    public double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    /**
     * Turns off the thermostat.
     */
    public void turnOff() {
        this.on = false;
        this.state = States.OFF;
    }

    /**
     * Sets the thermostat to idle state. In case it is off, then the thermostat is switched on.
     */
    public void setIdle() {
        this.on = true;
        this.state = States.IDLE;
    }

    /**
     * Turns on the thermostat computing the correct state
     */
    public void turnOn() {
        this.on = true;
        if (this.averageTemp > this.targetTemp) {
            this.state = States.COOLING;
        } else if (this.averageTemp < this.targetTemp) {
            this.state = States.HEATING;
        } else {
            this.state = States.IDLE;
        }
    }

    /**
     * The four possible states of thermostat.
     */
    private enum States {
        OFF, IDLE, HEATING, COOLING;
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
    public Thermostat makeCopy() {
        return new Thermostat(this);
    }
}
