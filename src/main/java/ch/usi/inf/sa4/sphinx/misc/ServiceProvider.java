package ch.usi.inf.sa4.sphinx.misc;


import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//if you are a teacher don't look at this
@Component
public class ServiceProvider {
    private static DeviceService staticDeviceService;
    private static UserService staticUserService;
    private static CouplingService staticCouplingService;
    private static RoomService staticRoomService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CouplingService couplingService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;



    public static DeviceService getStaticDeviceService() {
        return staticDeviceService;
    }

    public static UserService getStaticUserService() {
        return staticUserService;
    }

    public static CouplingService getStaticCouplingService() {
        return staticCouplingService;
    }

    public static RoomService getRoomService() {
        return staticRoomService;
    }

    @PostConstruct
    private void init() {
        staticCouplingService = couplingService;
        staticDeviceService = deviceService;
        staticRoomService = roomService;
        staticUserService = userService;
    }


}
