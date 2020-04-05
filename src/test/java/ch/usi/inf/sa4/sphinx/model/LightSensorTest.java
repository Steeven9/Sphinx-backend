package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.text.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.*;

class LightSensorTest {
=======

import static org.junit.jupiter.api.Assertions.*;

class LightSensorTest {

>>>>>>> #61: added tests for Effect, LightSensor and Light, SmartPlug and Devices improved
    @Disabled
    @Test
    void shouldReturnCorrectQuantityIfCallGetQuantity() {
        Sensor l = new LightSensor();
        String s = l.getLabel();
        DecimalFormat df = new DecimalFormat("#.#");
        int comma = 0;
        String s2 = "";
        int c = 0;
<<<<<<< HEAD
=======

        for(c = 0;comma <=0;c++){
            s2 += s.charAt(c);
            if(s.charAt(c) == '.'){
                comma++;
            }
        }
        s2+=s.charAt(c);

        Random random = new Random();
        double rd = random.nextDouble();
        double comparing = 500 + (rd - 0.5);
        String comparing_cutted = df.format(comparing);

        int comma2 = 0;
        String comparing_new = "";
        int i = 0;
        for(i = 0;comma2 <=0;c++){
            if(comparing_cutted.charAt(i) == ','){
                comparing_new += ".";
                comma2++;
            }else{
                comparing_new += s.charAt(i);
            }
        }
        comparing_new +=s.charAt(i);

        while(!s2.equals(comparing_cutted)){
            rd = random.nextDouble();
            comparing = 500.0 + (rd -0.5);
            comparing_cutted = df.format(comparing);
        }
        assertEquals(comparing_cutted + "" + l.getPhQuantity(), s2);
    }
>>>>>>> #61: added tests for Effect, LightSensor and Light, SmartPlug and Devices improved


        for (c = 0; comma <= 0; c++) {
            s2 += s.charAt(c);
            if (s.charAt(c) == '.') {
                comma++;
            }
        }
        s2 += s.charAt(c);

        Random random = new Random();
        double rd = random.nextDouble();
        double comparing = 500 + (rd - 0.5);
        String comparing_cutted = df.format(comparing);

        int comma2 = 0;
        String comparing_new = "";
        int i = 0;
        for (i = 0; comma2 <= 0; c++) {
            if (comparing_cutted.charAt(i) == ',') {
                comparing_new += ".";
                comma2++;
            } else {
                comparing_new += s.charAt(i);
            }
        }
        comparing_new += s.charAt(i);

        while (!s2.equals(comparing_cutted)) {
            rd = random.nextDouble();
            comparing = 500.0 + (rd - 0.5);
            comparing_cutted = df.format(comparing);
        }
        assertEquals(comparing_cutted + "" + l.getPhQuantity(), s2);
    }
}