package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Disabled;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockmvc;
    @Autowired
    private UserService userService;
    @Autowired
    private DummyDataAdder dummyDataAdder;

    @BeforeAll
    void init() {
        dummyDataAdder.addDummyData();
    }

    @Test
    public void testPosting() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("notExists")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(404));

        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("randUser")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    public void shouldGet400FromNoToken() throws Exception {
        this.mockmvc.perform(get("/guests/").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(get("/guests/houses").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(get("/guests/user1/devices").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

//        this.mockmvc.perform(get("/guests/user1/scenes").header("username", "user2"))
//                .andDo(print())
//                .andExpect(status().is(400));

        this.mockmvc.perform(post("/guests/").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(delete(("/guests/user2")).header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldGet401FromInvalidToken() throws Exception {
        this.mockmvc.perform(get("/guests/")
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/houses")
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/user2/devices")
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(get("/guests/user2/scenes")
//                .header("session-token", "banana")
//                .header("user", "user2"))
//                .andDo(print())
//                .andExpect(status().is(401));

        this.mockmvc.perform(post("/guests/")
                .header("session-token", "banana")
                .header("user", "user2")
                .content("user1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(delete(("/guests/user2"))
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldGet401FromWrongUser() throws Exception {
        this.mockmvc.perform(get("/guests/")
                .header("session-token", "user2SessionToken")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/houses")
                .header("session-token", "user2SessionToken")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/user2/devices")
                .header("session-token", "user2SessionToken")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(get("/guests/user2/scenes")
//                .header("session-token", "user2SessionToken")
//                .header("user", "fakeUser"))
//                .andDo(print())
//                .andExpect(status().is(401));

        this.mockmvc.perform(post("/guests/")
                .header("session-token", "user2SessionToken")
                .header("user", "fakeUser")
                .content("user1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(delete(("/guests/user2"))
                .header("session-token", "user2SessionToken")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldGet400OnPostFromNoBody() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldSuccessfullyReturnGuests() throws Exception {
        this.mockmvc.perform(get("/guests")
                .header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/guests")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldSuccessfullyReturnHousesAccess() throws Exception {
        this.mockmvc.perform(get("/guests/houses")
                .header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/guests/houses")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldGet500OnDeleteGuestWithWrongGuest() throws Exception {
        this.mockmvc.perform(delete("/guests/fakeUser")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(500));
    }

    @Test
    public void shouldSuccessfullyDeleteGuest() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("emptyUser")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(delete("/guests/emptyUser")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void shouldGet401OnGetGuestDevicesWithWrongGuest() throws Exception {
        this.mockmvc.perform(get("/guests/emptyUser/devices")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldGet401OnGetGuestDevicesWithNotExistingHost() throws Exception {
        this.mockmvc.perform(get("/guests/fakeUser/devices")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldSuccessfullyGetGuestDevices() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("user2")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/guests/user1/devices")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200));

    }

    @Test
    public void shouldSuccessfullyGetGuestDevicesSecurityCameras() throws Exception {
        // Create two new users
        User guest = new User("guest@smarthut.xyz", "1234", "guest", "Guest User");
        guest.setVerified(true);
        guest.setSessionToken("guestSessionToken");
        User host = new User("host@smarthut.xyz", "1234", "host", "Host User");
        host.setVerified(true);
        host.setSessionToken("hostSessionToken");
        userService.insert(guest);
        userService.insert(host);

        // Post Room to host
        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "hostSessionToken")
                .header("user", "host")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("host");
        Integer roomId = rooms.get(0).getId();

        // Add guest as guest of host
        this.mockmvc.perform(post("/guests/")
                .header("user", "host")
                .header("session-token", "hostSessionToken")
                .content("guest")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        // Enable host security cameras
        this.mockmvc.perform(put("/user/host")
                .header("session-token", "hostSessionToken")
                .content("{\"allowSecurityCameras\": " + true + "}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));

        // guest gets host devices with allowance to security cameras but with no security cameras
        this.mockmvc.perform(get("/guests/host/devices")
                .header("user", "guest")
                .header("session-token", "guestSessionToken"))
                .andDo(print())
                .andExpect(status().is(200));

        // Add a security camera to host
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "hostSessionToken")
                .header("user", "host")
                .content("{\"name\":\"camera\",\"icon\":\"/images/generic_device\", \"type\":\"13\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // guest gets host devices with allowance to security cameras and with a security camera
        this.mockmvc.perform(get("/guests/host/devices")
                .header("user", "guest")
                .header("session-token", "guestSessionToken"))
                .andDo(print())
                .andExpect(status().is(200));

        // Disable host security cameras
        this.mockmvc.perform(put("/user/host")
                .header("session-token", "hostSessionToken")
                .content("{\"allowSecurityCameras\": " + false + "}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));

        // guest gets host devices without allowance to security cameras and with a security camera
        this.mockmvc.perform(get("/guests/host/devices")
                .header("user", "guest")
                .header("session-token", "guestSessionToken"))
                .andDo(print())
                .andExpect(status().is(200));

        // Delete guest as guest of host
        this.mockmvc.perform(delete("/guests/guest")
                .header("user", "host")
                .header("session-token", "hostSessionToken"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void shouldGet401OnGetGuestScenesWithWrongGuest() throws Exception {
        User scenesHost = new User("sceneshost1@smarthut.xyz", "1234", "ScenesHost1", "Post Scene");
        scenesHost.setVerified(true);
        scenesHost.setSessionToken("SHST");
        userService.insert(scenesHost);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "SHST")
                .header("user", "ScenesHost1")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("ScenesHost1");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "SHST")
                .header("user", "ScenesHost1")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "SHST")
                .header("user", "ScenesHost1")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/guests/ScenesHost1/scenes")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldGet401OnGetGuestScenesWithNotExistingHost() throws Exception {
        this.mockmvc.perform(get("/guests/fakeUser/scenes")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldGet401OnGetGuestScenesWithoutPermission() throws Exception {
        User scenesHost = new User("sceneshost3@smarthut.xyz", "1234", "ScenesHost3", "Post Scene");
        scenesHost.setVerified(true);
        scenesHost.setSessionToken("SHST");
        userService.insert(scenesHost);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "SHST")
                .header("user", "ScenesHost3")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("ScenesHost3");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "SHST")
                .header("user", "ScenesHost3")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "SHST")
                .header("user", "ScenesHost3")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }], \"shared\": \"false\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int scenesId = userService.getByMail("sceneshost3@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(post("/guests/")
                .header("user", "ScenesHost3")
                .header("session-token", "SHST")
                .content("user2")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/scenes/" + scenesId)
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldSuccessfullyGetGuestScenes() throws Exception {
        User scenesHost = new User("sceneshost2@smarthut.xyz", "1234", "ScenesHost2", "Post Scene");
        scenesHost.setVerified(true);
        scenesHost.setSessionToken("SHST");
        userService.insert(scenesHost);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "SHST")
                .header("user", "ScenesHost2")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("ScenesHost2");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "SHST")
                .header("user", "ScenesHost2")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "SHST")
                .header("user", "ScenesHost2")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }], \"shared\": \"true\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(post("/guests/")
                .header("user", "ScenesHost2")
                .header("session-token", "SHST")
                .content("user2")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/guests/ScenesHost2/scenes")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int scenesId = userService.getByMail("sceneshost2@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(get("/scenes/" + scenesId)
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

}
