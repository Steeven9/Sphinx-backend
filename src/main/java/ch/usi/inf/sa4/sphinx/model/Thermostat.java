package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import com.google.gson.annotations.Expose;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

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

    public States getState() {
        return state;
    }

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

    /**
     * @param s a State of thermostat
     * @return int mapping to it
     */
    private int fromStateToInt(States s) {
        switch (s) {
            case IDLE:
                return 1;
            case OFF:
                return 0;
            case COOLING:
                return 2;
            case HEATING:
                return 3;
            default:
                return -1;
        }
    }

    public Sources getSource() {
        return source;
    }

    /**
     * Returns the average temperature in this room. It is computed as the media of all temperature sensors and
     * temperature of this Thermostat.
     *
     * @return the average temperature
     */
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
     * {@inheritDoc}
     */
    @Override
    protected SerialisableDevice serialise() {
        SerialisableDevice sd = super.serialise();
        sd.targetTemp = this.targetTemp;
        sd.averageTemp = this.getAverageTemp();
        sd.stateTemp = this.fromStateToInt(this.getState());
        sd.source = this.source == Sources.SELF ? 0 : 1;
        return sd;
    }

    public void setState(States state) {
        this.state = state;
    }

    public void setSource(int source) {
        if (source == 0) {
            this.source = Sources.SELF;
        } else {
            this.source = Sources.AVERAGE;
        }

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
    public String getLabel() {
        return new DecimalFormat("#.##").format(this.getValue()) + " " + this.getPhQuantity() + "State: " + this.getState() + " Avg: " + this.getAverageTemp();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.THERMOSTAT;
    }
}
