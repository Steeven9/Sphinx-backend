package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThermostatTest {
    Thermostat thermostat;

    @BeforeEach
    void set() {
        thermostat = new Thermostat();
        thermostat.setRoom(new Room());
    }

    @Test
    void testGettersAndSetters() {
        Thermostat tmp = new Thermostat();
        assertNotNull(tmp);
        assertAll(
                () -> assertEquals(Thermostat.States.IDLE, thermostat.getState()),
                () -> assertEquals(Thermostat.Sources.SELF, thermostat.getSource()),
                () -> assertEquals(DeviceType.THERMOSTAT, thermostat.getDeviceType()),
                () -> assertEquals(20.0, thermostat.getQuantity()),
                () -> assertEquals(20.0, thermostat.getLastValue()),
                () -> assertEquals(1.0, thermostat.getTolerance())
        );

        thermostat.setSource(1);
        assertEquals(Thermostat.Sources.AVERAGE, thermostat.getSource());
        thermostat.setSource(0);
        assertEquals(Thermostat.Sources.SELF, thermostat.getSource());

        thermostat.setQuantity(10.0);
        assertEquals(10.0, thermostat.getQuantity());

        thermostat.setTolerance(3.0);
        assertEquals(3.0, thermostat.getTolerance());
        assertThrows(IllegalArgumentException.class, () -> thermostat.setTolerance(-1));
    }

    @RepeatedTest(10)
    void testToleranceError(RepetitionInfo repetitionInfo) {
        thermostat.setTolerance(repetitionInfo.getCurrentRepetition());
        thermostat.setQuantity(repetitionInfo.getCurrentRepetition() * 3);
        thermostat.generateValue();
        double quantity = thermostat.getQuantity();
        double tolerance = thermostat.getTolerance();
        double lastValue = thermostat.getLastValue();
        assertTrue(lastValue <= quantity + tolerance && lastValue >= quantity - tolerance);
    }

    @Test
    void testSetTargetTemperature() {
        thermostat.setTargetTemp(100);
        assertEquals(Thermostat.States.HEATING, thermostat.getState());
        assertEquals(100, thermostat.getTargetTemp());

        thermostat.setSource(1);
        thermostat.setTolerance(4);
        thermostat.setTargetTemp(23); // testing for idle
        assertEquals(Thermostat.States.IDLE, thermostat.getState());
        thermostat.setTargetTemp(19);
        assertEquals(Thermostat.States.IDLE, thermostat.getState());

        thermostat.setTargetTemp(10);
        assertEquals(Thermostat.States.COOLING, thermostat.getState());
    }

    @Test
    void testSwitches() {
        thermostat.turnOff();
        assertEquals(Thermostat.States.OFF, thermostat.getState());
        assertFalse(thermostat.isOn());

        thermostat.turnOn();
        assertEquals(Thermostat.States.IDLE, thermostat.getState());
        assertTrue(thermostat.isOn());

        thermostat.setIdle();
        assertEquals(Thermostat.States.IDLE, thermostat.getState());
        assertTrue(thermostat.isOn());

        thermostat.setTargetTemp(100);
        thermostat.turnOn();
        assertEquals(Thermostat.States.HEATING, thermostat.getState());

        thermostat.turnOff();
        thermostat.setTargetTemp(2);
        thermostat.turnOn();
        assertEquals(Thermostat.States.COOLING, thermostat.getState());
    }

    @Test
    void testSerializer() {
        SerialisableDevice sd = thermostat.serialise();
        double quantity = thermostat.getQuantity();
        double tolerance = thermostat.getTolerance();
        assertAll(
                () -> assertEquals(sd.slider, thermostat.getTargetTemp()),
                () -> assertEquals(1, sd.state),
                () -> assertEquals(0, sd.source),
                () -> assertEquals(thermostat.getTolerance(), sd.tolerance),
                () -> assertEquals(thermostat.getQuantity(), sd.quantity),
                () -> assertTrue(sd.averageTemp <= quantity + tolerance && sd.averageTemp >= quantity - tolerance)
        );

        thermostat.setTargetTemp(100);
        assertEquals(3, thermostat.serialise().state);

        thermostat.setTargetTemp(1);
        assertEquals(2, thermostat.serialise().state);

        thermostat.turnOff();
        assertEquals(0, thermostat.serialise().state);
    }

    @Test
    void testAverage() {
        Room room = new Room();
        room.addDevice(new HumiditySensor());

        TempSensor temp = new TempSensor();
        temp.setQuantity(40.0);
        temp.generateValue();
        room.addDevice(temp);

        TempSensor temp2 = new TempSensor();
        temp2.setTolerance(3);
        temp2.generateValue();
        room.addDevice(temp2);

        thermostat.setRoom(room);
        thermostat.setSource(1);
        double average = thermostat.getAverageTemp();

        double quantity1 = temp.getQuantity();
        double quantity2 = temp2.getQuantity();
        double quantity3 = thermostat.getQuantity();
        double tolerance1 = temp.getTolerance();
        double tolerance2 = temp2.getTolerance();
        double tolerance3 = thermostat.getTolerance();

        double meanTolerance = (tolerance1 + tolerance2 + tolerance3) / 3;
        double meanQuantity = (quantity1 + quantity2 + quantity3) / 3;
        assertTrue(average <= meanQuantity + meanTolerance && average >= meanQuantity - meanTolerance);
    }
}