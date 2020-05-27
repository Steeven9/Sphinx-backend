package ch.usi.inf.sa4.sphinx.misc;


import ch.usi.inf.sa4.sphinx.service.*;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Provides access to the Service layer.
 */
//if you are a teacher don't look at this
@Component
public class ServiceProvider {
    private static DeviceService staticDeviceService;
    private static UserService staticUserService;
    private static CouplingService staticCouplingService;
    private static RoomService staticRoomService;
    private static AutomationService staticAutomationService;

    private ServiceProvider(@NonNull final DeviceService deviceService, @NonNull final CouplingService couplingService,
                            @NonNull final UserService userService, @NonNull final RoomService roomService
            , @NonNull final AutomationService automationService) {

        staticDeviceService = deviceService;
        staticCouplingService = couplingService;
        staticUserService = userService;
        staticRoomService = roomService;
        staticAutomationService = automationService;
    }


    /**
     * @return The DeviceService
     * @see DeviceService
     */
    public static DeviceService getDeviceService() {
        return staticDeviceService;

    }

    /**
     * @return The UserService
     * @see UserService
     */
    public static UserService getUserService() {
        return staticUserService;
    }

    /**
     * @return The CouplingService
     * @see CouplingService
     */
    public static CouplingService getCouplingService() {
        return staticCouplingService;
    }

    /**
     * @return The RoomService
     * @see RoomService
     */
    public static RoomService getRoomService() {
        return staticRoomService;
    }

    /**
     * @return The AutomationService
     * @see AutomationService
     */
    public static AutomationService getAutomationService() {
        return staticAutomationService;
    }

}