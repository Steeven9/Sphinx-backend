package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import java.util.List;

/**
 * A Thermostat is a device, which can control the temperature in a given room. It is embedded to a Temperature sensor.
 */
@Entity
public class Thermostat extends TempSensor {
    @Expose//TODO the only information being saved atm
    private double targetTemp;
    private States state;
    private Sources source;

    /**
     * Constructor.
     * Initialize a thermostat, which is on and has idle as internal state. Its initial is set to its actual value.
     */
    public Thermostat() {
        super();
        this.targetTemp = this.getLastValue();
        this.state = States.IDLE;
        this.source = Sources.SELF;
    }

    /**
     * Returns the current state of this Thermostat.
     *
     * @return the state of this thermostat
     */
    public States getState() {
        return state;
    }

    /**
     * Returns the Source of this Thermostat: Self or Average.
     *
     * @return the source of thermostat
     */
    public Sources getSource() {
        return source;
    }

    /**
     * Returns target temperature of this thermostat.
     *
     * @return target temperature
     */
    public double getTargetTemp() {
        return targetTemp;
    }

    /**
     * Returns a State basing on given target temperature and considering the Source.
     *
     * @param target the target temperature
     * @return the State of Thermostat
     */
    private States determineState(final double target) {
        final double temp;
        if (this.source == Sources.SELF) {
            temp = this.getLastValue();
        } else {
            temp = this.getAverageTemp();
        }

        double tolerance = this.getTolerance();
        if (temp <= target + tolerance && temp >= target - tolerance) {
            return States.IDLE;
        } else {
            if (target > temp) {
                return States.HEATING;
            } else {
                return States.COOLING;
            }
        }
    }

    /**
     * Sets target temperature of a given Thermostat.
     *
     * @param target target temperature to be set
     */
    public void setTargetTemp(final double target) {
        this.state = this.determineState(target);
        this.targetTemp = target;
    }

    /**
     * Maps a state of thermostat to an int.
     *
     * @param state a State of thermostat
     * @return int mapping to thermostat
     */
    private static int fromStateToInt(final States state) {
        switch (state) {
            case IDLE:
                return 1;
            case COOLING:
                return 2;
            case HEATING:
                return 3;
            default:
                return 0; // off
        }
    }


    /**
     * Returns the average temperature in this room. It is computed as the average of all temperature sensors and
     * temperature of this Thermostat.
     *
     * @return the average temperature
     */
    public double getAverageTemp() {
        final List<Device> devices = this.getRoom().getDevices();
        double averageTemp = 0.0;
        double sensors = 1.0;

        if (!(devices.isEmpty())) {
            for (final Device device : devices) {
                if (device.getDeviceType() == DeviceType.TEMP_SENSOR) {
                    averageTemp += ((TempSensor) device).getLastValue();
                    sensors++;
                }
            }
        }

        averageTemp += this.getLastValue(); //gets last value since all sensors have been updated
        averageTemp /= sensors;

        return averageTemp;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisableDevice serialise() {
        final SerialisableDevice sd = super.serialise();
        sd.setSlider(this.targetTemp);
        sd.setAverageTemp(this.getAverageTemp());
        sd.setState(Thermostat.fromStateToInt(this.state));
        sd.setSource(this.source == Sources.SELF ? 0 : 1);
        return sd;
    }


    /**
     * Sets a thermostat to a given source. The source should be in int: 0 Self and 1 for Average.
     *
     * @param source the source in int to set
     */
    public void setSource(final int source) {
        if (source == 0) {
            this.source = Sources.SELF;
        } else {
            this.source = Sources.AVERAGE;
        }

    }

    /**
     * Sets the thermostat to idle state. In case it is off, then the thermostat is switched on.
     */
    public void setIdle() {
        this.on = true;
        this.state = States.IDLE;
    }

    @Override
    public void setOn(final boolean on) {
        if (on) {
            this.on = true;
            this.state = this.determineState(this.targetTemp);
        } else {
            this.on = false;
            this.state = States.OFF;
        }
    }

    /**
     * The four possible states of thermostat.
     */
    public enum States {
        OFF, IDLE, HEATING, COOLING
    }

    /**
     * The two possible sources of thermostat.
     */
    public enum Sources {
        SELF, AVERAGE
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.THERMOSTAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override


    public void setPropertiesFrom(final SerialisableDevice sd) {
        super.setPropertiesFrom(sd);
        if (sd.getSource() != null) setSource(sd.getSource());
        if (sd.getSlider() != null) setTargetTemp(sd.getSlider());


    }
}
