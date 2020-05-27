package ch.usi.inf.sa4.sphinx.misc;
import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ServiceProviderTest {

    @Test
    void coverServiceProvider() {
        ServiceProvider.getUserService();
        ServiceProvider.getDeviceService();
        ServiceProvider.getCouplingService();
        ServiceProvider.getRoomService();
    }
}