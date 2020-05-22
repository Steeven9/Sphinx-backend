package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeviceControllerTest {

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
    void shouldNotAcceptIncompleteOrInconsistentData() throws Exception {
        //get
        this.mockmvc.perform(get("/devices/test"))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(get("/devices/test").header("user", ""))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(get("/devices/test").content("{\"name\": \"\"}"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(get("/devices/123456789")
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().isNotFound());

        //post
        this.mockmvc.perform(post("/devices/").content("{\"icon\" : \"\", \"name\": \"\",\"type\": \"\",\"roomId\": \"\"}")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(post("/devices/").content("{\"name\": \"\"}")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(post("/devices/").content("{\"roomId\": \"\"}")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(post("/devices/").content("{\"type\": \"\"}")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(post("/devices/couple/null/1234")
                .content("{}")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        //put
        this.mockmvc.perform(put("/devices/test").content("{\"name\": \"\"}"))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(put("/devices/test").content("{\"deviceId\": \"\"}"))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(put("/devices/test").content("{\"icon\": \"\"}"))
                .andDo(print()).andExpect(status().is(400));

        //delete
        this.mockmvc.perform(delete("/devices/test").header("user", ""))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(delete("/devices/test").content("\"deviceId\": \"\""))
                .andDo(print()).andExpect(status().is(400));

    }


    @Test
    void shouldReturn400WithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(get("/devices/test")).andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void shouldNotCreateWithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(post("/devices/")).andDo(print())
                .andExpect(status().is(400));
        this.mockmvc.perform(post("/devices/")
                .header("user", "")).andDo(print()).andExpect(status().is(400));
    }

    @Test
    void shouldNotEditWithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(put("/devices/test")).andDo(print())
                .andExpect(status().is(400));
        this.mockmvc.perform(put("/devices/test")
                .header("user", "")).andDo(print()).andExpect(status().is(400));
    }

    @Test
    void shouldNotDeleteWithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(delete("/devices/test").header("user", "user2")).andDo(print())
                .andExpect(status().is(400));


        this.mockmvc.perform(delete("/devices/test")
                .header("session-token", "user2SessioToken")).andDo(print()).andExpect(status().is(400));
        ;
    }


    @Test
    void shouldSuccessfullyGetOnValidData() throws Exception {
        this.mockmvc.perform(get("/devices/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void testPostingNewDeviceAndMigrateToAnotherRoom() throws Exception {
        List<Room> rooms = userService.getPopulatedRooms("user2");
        Integer roomId1 = rooms.get(0).getId();
        Integer roomId2 = rooms.get(1).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2")
                .content("{\"name\":\"lamp\",\"icon\":\"/images/generic_device\", \"type\":\"1\",\"roomId\":\"" + roomId1 + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = this.mockmvc.perform(post("/devices/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2")
                .content("{\"name\":\"thermostat\",\"icon\":\"/images/generic_device\", \"type\":\"11\",\"roomId\":\"" + roomId1 + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String[] content = mvcResult.getResponse().getContentAsString().split("[{\\}\\:\\,]");

        Integer thermostatId = 0;
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals("\"id\"")) {
                thermostatId = Integer.parseInt(content[i + 1]);
            } else if (content[i].equals("\"roomId\"")) {
                content[i + 1] = roomId2.toString(); //change room id
                break;
            }
        }
        StringBuilder contentPut = new StringBuilder("{");
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals("\"type\"")) i += 2;
            if (content[i].equals("\"label\"")) i += 2;
            if (content[i].equals("\"switched\"")) i += 2;
            if (content[i].equals("\"switches\"")) i += 2;
            if (content[i].equals("\"roomName\"")) i += 2;
            if (i % 2 == 0 && i != 0) { // add value
                contentPut.append(":");
                contentPut.append(content[i]);
                contentPut.append(",");
            } else {
                contentPut.append(content[i]);
            }
        }
        contentPut.deleteCharAt(contentPut.length() - 1);
        contentPut.append("}");
        String res = contentPut.toString();
        this.mockmvc.perform(put("/devices/" + thermostatId)
                .header("session-token", "user2SessionToken")
                .header("user", "user2")
                .content(res)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));
    }


    @Test
    void shouldSuccessfullyPutAndGetOnValidData() throws Exception {
        Room room = userService.getPopulatedRooms("user2").get(0);
        Device device = room.getDevices().get(0);

        this.mockmvc.perform(put("/devices/" + device.getId())
                .header("session-token", "user2SessionToken")
                .header("user", "user2")
                .content("{\"name\":\"someRandName\", \"icon\":\"/images/generic_device\",\"on\":\"false\",\"roomId\":\"" + room.getId() + "\",\"devices\":[]}")
                .contentType("application/json")).andDo(print())
                .andExpect(status().is(200));

        MvcResult mvcResult = this.mockmvc.perform(get("/devices/" + device.getId())
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String[] content = mvcResult.getResponse().getContentAsString().split("[:,]");
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals("\"name\"")) {
                assertEquals("\"someRandName\"", content[++i]);
                break;
            }
        }
    }


    @Test
    void testDeleteDevice() throws Exception {
        Room room = userService.getPopulatedRooms("user2").get(0);
        Integer deviceId = room.getDevices().get(1).getId();
        this.mockmvc.perform(delete("/devices/" + deviceId)
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().isNoContent());

        room = userService.getPopulatedRooms("user1").get(0);
        deviceId = room.getDevices().get(0).getId();
        this.mockmvc.perform(delete("/devices/" + deviceId)
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGet401WithWrongUser() throws Exception {
        this.mockmvc.perform(get("/devices/")
                .header("user", "user1")
                .header("session-token", "user2SessionToken"))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturn401OnNotValidToken() throws Exception {


        this.mockmvc.perform(get("/devices/")
                .header("user", "user2")
                .header("session-token", "token")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));


        this.mockmvc.perform(post("/devices/")
                .header("user", "user2")
                .header("session-token", "token")
                .content("{\"name\":\"lamp\",\"icon\":\"/images/generic_device\", \"type\": \"1\",\"roomId\":\"3\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(put("/devices/3")
                .header("user", "user2")
                .header("session-token", "token")
                .content("{\"name\":\"newLight\", \"icon\":\"/images/generic_device\",\"on\":\"false\",\"roomId\":\"2\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }


    @Test
    void shouldReturn400OnIdOrTypeMissing() throws Exception {
        this.mockmvc.perform(post("/devices/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("{\"name\":\"newLight\", \"icon\":\"/images/generic_device\",\"type\":\"1\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(post("/devices/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("{\"name\":\"newLight\", \"icon\":\"/images/generic_device\",\"roomId\":\"4\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400));

    }

    @Test
    void shouldReturn401OnWrongDevice() throws Exception {
        Room room = userService.getPopulatedRooms("user2").get(0);
        Integer deviceId = room.getDevices().get(1).getId();
        this.mockmvc.perform(post("/devices/")
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{\"name\":\"nameLight\", \"icon\":\"/images/generic_device\",\"type\":\"1\", \"roomId\":\"" + room.getId() + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/devices/" + deviceId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(put("/devices/" + deviceId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(put("/devices/reset/" + deviceId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(post("/devices/couple/1234435/65432")
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(delete("/devices/couple/12354345/4567876")
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void resetSmartPlug() throws Exception {
        List<Room> rooms = userService.getPopulatedRooms("user2");
        Integer plugId = 0;
        Integer notPlugId = 0;
        for (Room room : rooms) {
            List<Device> devices = room.getDevices();
            for (int i = 0; i < devices.size(); i++) {
                Device device = devices.get(i);
                if (DeviceType.deviceToDeviceType(device) == DeviceType.SMART_PLUG) {
                    plugId = device.getId();
                    break;
                } else {
                    notPlugId = device.getId();
                }
            }
            if (plugId != 0) break;
        }

        this.mockmvc.perform(put("/devices/reset/" + notPlugId)
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(put("/devices/reset/" + plugId)
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Disabled(value = "fix remove couplings")
    void testCoupling() throws Exception {
        List<Room> rooms = userService.getPopulatedRooms("user1");
        Room room = rooms.get(0);
        Device light = room.getDevices().get(0);

        MvcResult mvcResult = this.mockmvc.perform(post("/devices/")
                .header("session-token", "user1SessionToken")
                .header("user", "user1")
                .content("{\"name\":\"switch\",\"icon\":\"/images/generic_device\", \"type\":\"3\",\"roomId\":\"" + room.getId() + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String[] content = mvcResult.getResponse().getContentAsString().split("[:,]");
        int switchId = 0;
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals("{\"id\"")) {
                String str = content[i + 1];
                switchId = Integer.parseInt(str);
                break;
            }
        }

        this.mockmvc.perform(post("/devices/couple/" + light.getId() + "/" + switchId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockmvc.perform(delete("/devices/couple/" + light.getId() + "/" + switchId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("{}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Disabled(value = "401 when adding a guest to user 1")
    void increaseGetConditionCoverage() throws Exception {
        List<Room> rooms = userService.getPopulatedRooms("user1");
        Room room = rooms.get(0);

        MvcResult mvcResult = this.mockmvc.perform(post("/devices/")
                .header("session-token", "user1SessionToken")
                .header("user", "user1")
                .content("{\"name\":\"switch\",\"icon\":\"/images/generic_device\", \"type\":\"13\",\"roomId\":\"" + room.getId() + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String[] content = mvcResult.getResponse().getContentAsString().split("[:,]");
        int cameraId = 0;
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals("{\"id\"")) {
                String str = content[i + 1];
                cameraId = Integer.parseInt(str);
                break;
            }
        }

        this.mockmvc.perform(get("/devices/" + cameraId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));

        this.mockmvc.perform(put("/user/user1")
                .header("session-token", "user1SessionToken")
                .content("{\"email\": \"test1@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test2\", \"allowSecurityCameras\": \"true\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));

        this.mockmvc.perform(get("/devices/" + cameraId)
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(post("/guests/") //error 401
                .header("user", "user1")
                .header("session-token", "user1SessionToken")
                .content("emptyUser")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/devices/" + room.getDevices().get(0).getId()) // light
                .header("user", "emptyUser")
                .header("session-token", "emptyUserSessionToken")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }
}