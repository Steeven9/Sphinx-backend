package ch.usi.inf.sa4.sphinx.misc;


import ch.usi.inf.sa4.sphinx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CouplingService couplingService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private AutomationService automationService;

    public ServiceProvider(@NonNull final DeviceService deviceService, @NonNull final CouplingService couplingService,
                           @NonNull final UserService userService, @NonNull final RoomService roomService) {
        staticDeviceService = deviceService;
        staticCouplingService = couplingService;
        staticUserService = userService;
        staticRoomService = roomService;
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
    public static AutomationService getAutomationService(){
        return staticAutomationService;
    }

    @PostConstruct
    private void init() {
        staticCouplingService = couplingService;
        staticDeviceService = deviceService;
        staticRoomService = roomService;
        staticUserService = userService;
        staticAutomationService = automationService;
    }


}
