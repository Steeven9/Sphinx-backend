package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.text.DecimalFormat;
import java.util.List;

/**
 * A Thermostat is a device, which can control the temperature in a given room. It is embedded to a Temperature sensor.
 */
@Entity
public class Thermostat extends TempSensor {

    @Expose
    private double targetTemp;
    @Transient
    private double averageTemp;
    @Transient
    private States state;
    @Transient
    private Sources source;

    /**
     * Constructor.
     * Initialize a thermostat, which is on and has idle as internal state. Its initial is set to its actual value.
     */
    public Thermostat() {
        super();
        this.targetTemp = this.getValue();
        this.state = States.IDLE;
        this.averageTemp = this.targetTemp;
        this.source = Sources.SELF;
    }

    /**
     * Sets a target temperature to this thermostat and changes the internal state.
     *
     * @param target the target temperature
     */
    public void setTargetTemp(double target) {
        double temp;
        if (Sources.SELF.equals(this.source)) {
            temp = this.getValue();
        } else {
            temp = getAverageTemp();
        }
        if (target > temp) {
            this.state = States.HEATING;
        } else if (target < temp) {
            this.state = States.COOLING;
        }
        this.targetTemp = target;
    }


    public States getState() {
        return state;
    }

    public Sources getSource() {
        return source;
    }

    public double getAverageTemp() {
        List<Device> devices = this.getRoom().getDevices();
        double averageTemp = 0.0, sensors = 0.0;

        if (!(devices.size() == 0)) {
            for (Device device : devices) {
                if (DeviceType.deviceToDeviceType(device) == DeviceType.TEMP_SENSOR) {
                    averageTemp += ((TempSensor) device).getValue();
                    sensors++;
                }
            }
        }

        ++sensors;
        averageTemp += this.getValue();
        averageTemp = averageTemp / sensors;

        this.averageTemp = averageTemp;
        return averageTemp;
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
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return new DecimalFormat("#.##").format(this.getValue()) + " " + this.getPhQuantity() + "State: " + this.getState() + " Avg: " + this.getAverageTemp();
    }

    /**
     * The four possible states of thermostat.
     */
    private enum States {
        OFF, IDLE, HEATING, COOLING
    }

    /**
     * The two possible sources of thermostat.
     */
    private enum Sources {
        SELF, AVERAGE
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.THERMOSTAT;
    }
}
