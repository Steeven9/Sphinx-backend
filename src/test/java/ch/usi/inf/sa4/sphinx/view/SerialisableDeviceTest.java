package ch.usi.inf.sa4.sphinx.view;



import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;


import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class SerialisableDeviceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;


    SerialisableDevice serialisableDevice;

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                arguments(101, "testIcon", "testName", "testLabel", new int[10], new int[]{3, 6}, 1.0, 666, 1, true, "url", 20, 23, 43, 1, 0),
                arguments(0, "__testIcon", "testName", "testLabel", new int[1], new int[]{1, 2, 4, 6}, 1.0, 666, 1, true, "URL!!", 0, -10, 4444, 1, 0),
                arguments(984354, "#testIcon", "testName", "testLabel", new int[10000], new int[]{0}, 0.0, 666, 1, true, "Noturl", -20, 2222, -111, 1, 0)
        );
    }

    @Test
    @DisplayName("Testing the creation of constructor without parameters")
    void existSerializedDevice() {
        serialisableDevice = new SerialisableDevice();
        assertNotNull(serialisableDevice);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    @DisplayName("Testing constructor with multiple arguments")
    void testLongConstructor(Integer id, String icon, String name, String label, int[] switched, int[] switches, double intensity, Integer roomId, int type, boolean on, String url, double quantity, double averageTemp, double targetTemp, int stateTemp, int source) {
        serialisableDevice = new SerialisableDevice(id, icon, name, label, switched, switches, intensity, roomId, type, on, url, quantity, averageTemp, targetTemp, stateTemp, source);
        assertAll("Should return new SerialisableDevice",
                () -> assertEquals(id, serialisableDevice.id),
                () -> assertEquals(name, serialisableDevice.name),
                () -> assertEquals(label, serialisableDevice.label),
                () -> assertArrayEquals(switched, serialisableDevice.switched),
                () -> assertArrayEquals(switches, serialisableDevice.switches),
                () -> assertEquals(icon, serialisableDevice.icon),
                () -> assertEquals(intensity, serialisableDevice.slider),
                () -> assertEquals(roomId, serialisableDevice.roomId),
                () -> assertEquals(type, serialisableDevice.type),
                () -> assertEquals(on, serialisableDevice.on),
                () -> assertEquals(url, serialisableDevice.url),
                () -> assertEquals(source, serialisableDevice.source),
                () -> assertEquals(stateTemp, serialisableDevice.stateTemp),
                () -> assertEquals(quantity, serialisableDevice.quantity),
                () -> assertEquals(averageTemp, serialisableDevice.averageTemp),
                () -> assertEquals(targetTemp, serialisableDevice.targetTemp),
                () -> assertEquals(name, serialisableDevice.name)
        );
    }

}