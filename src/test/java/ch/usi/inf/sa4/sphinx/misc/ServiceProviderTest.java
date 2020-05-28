package ch.usi.inf.sa4.sphinx.misc;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ServiceProviderTest {

    @Test
    void test() {
        assertAll(
                () -> assertNotNull(ServiceProvider.getDeviceService()),
                () -> assertNotNull(ServiceProvider.getAutomationService()),
                () -> assertNotNull(ServiceProvider.getCouplingService()),
                () -> assertNotNull(ServiceProvider.getRoomService()),
                () -> assertNotNull(ServiceProvider.getUserService())
        );
    }
}