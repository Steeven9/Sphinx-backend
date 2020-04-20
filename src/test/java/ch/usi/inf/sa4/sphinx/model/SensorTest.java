package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.text.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

class SensorTest {

    @Test
    void shouldCreateANewSensorUsingAnotherLightSensorInConstructor(){
        LightSensor s = new LightSensor();
        LightSensor s2 = new LightSensor(s);
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldCreateANewLightSensorUsingMakeCopy(){
        LightSensor s = new LightSensor();
        LightSensor s2 = s.makeCopy();
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldReturnCorrectQuantityIfCallGetQuantityLightSensor() {
        LightSensor s = new LightSensor();
        for(int i=0; i <= 10;i++){
            double value = s.getValue();
            assertFalse(value < 499.5);
            assertFalse(value >500.5);
        }
    }

    @Test
    void shouldReturnCorrectLabelLightSensor(){
        LightSensor s = new LightSensor();
        String label = s.getLabel();
        for(int i =0; i<=10;i++){
            double value = s.getValue();
            assertTrue(label.contains("lm"));
            if(label.contains("500")){
                assertTrue(label.contains("500"));
            }else{
                assertTrue(label.contains("499"));
            }
        }
    }
    /* Temp Sensor Tests */
    @Test
    void shouldCreateANewSensorUsingAnotherSensorInConstructorTemp(){
        TempSensor s = new TempSensor();
        TempSensor s2 = new TempSensor(s);
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldCreateANewSensorUsingMakeCopyTemp(){
        TempSensor s = new TempSensor();
        TempSensor s2 = s.makeCopy();
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldReturnCorrectQuantityIfCallGetQuantityTemp() {
        TempSensor s = new TempSensor();
        for(int i=0; i <= 10;i++){
            double value = s.getValue();
            assertFalse(value < 19);
            assertFalse(value >20.5);
        }
    }

    @Test
    void shouldReturnCorrectLabelTemp(){
        TempSensor s = new TempSensor();
        String label = s.getLabel();
        for(int i =0; i<=10;i++){
            double value = s.getValue();
            assertTrue(label.contains("C"));
            if(label.contains("20")){
                assertTrue(label.contains("20"));
            }else{
                assertTrue(label.contains("19"));
            }
        }
    }

    /* Humidity Sensor Tests */
    @Test
    void shouldCreateANewSensorUsingAnotherSensorInConstructorHumidity(){
        HumiditySensor s = new HumiditySensor();
        HumiditySensor s2 = new HumiditySensor(s);
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldCreateANewSensorUsingMakeCopyHumidity(){
        HumiditySensor s = new HumiditySensor();
        HumiditySensor s2 = s.makeCopy();
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldReturnCorrectQuantityIfCallGetQuantityHumidity() {
        HumiditySensor s = new HumiditySensor();
        for(int i=0; i <= 10;i++){
            double value = s.getValue();
            assertFalse(value < 31);
            assertFalse(value > 32.5);
        }
    }

    @Test
    void shouldReturnCorrectLabelHumidity(){
        HumiditySensor s = new HumiditySensor();
        String label = s.getLabel();
        for(int i =0; i<=10;i++){
            double value = s.getValue();
            assertTrue(label.contains("%"));
            if(label.contains("32")){
                assertTrue(label.contains("32"));
            }else{
                assertTrue(label.contains("31"));
            }
        }
    }
    /* Motion Sensor Tests */
    @Test
    void shouldCreateANewSensorUsingAnotherSensorInConstructorMotion(){
        MotionSensor s = new MotionSensor();
        MotionSensor s2 = new MotionSensor(s);
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }

    @Test
    void shouldCreateANewSensorUsingMakeCopyMotion(){
        MotionSensor s = new MotionSensor();
        MotionSensor s2 = s.makeCopy();
        assertNotNull(s2);
        assertEquals(s2.getId(), s.getId());
        assertEquals(s2.getName(), s.getName());
        assertEquals(s2.getIcon(), s.getIcon());
    }


    @Test
    void shouldReturnCorrectLabelMotion(){
        MotionSensor s = new MotionSensor();
        s.notDetected();
        String label = s.getLabel();
        assertTrue(label.contains("false"));

        s.detected();
        String label2 = s.getLabel();
        assertTrue(label2.contains("true"));
    }
}