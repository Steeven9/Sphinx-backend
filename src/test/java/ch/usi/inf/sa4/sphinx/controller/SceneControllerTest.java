package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Effect;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableScene;
import ch.usi.inf.sa4.sphinx.view.SerialisableSceneEffect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SceneControllerTest {

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
    void testErrorsOnScenes() throws Exception {
        SceneController sceneC = new SceneController();
        BindException error = new BindException(new Object(), "something");
        error.addError(new ObjectError("something", "something"));
        List<SerialisableSceneEffect> effects = new ArrayList<>();

        User errUser = new User("scenes0@smarthut.xyz", "1234", "Scenes0", "Post Scene");
        errUser.setVerified(true);
        errUser.setSessionToken("ScenesST");
        userService.insert(errUser);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes0");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        // POST
        assertThrows(NullPointerException.class, () -> sceneC.createScene(null, null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.createScene("gno", null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.createScene("gno gno", "gno", null, null));
        assertThrows(BadRequestException.class, () ->
                sceneC.createScene("ScenesST", "Scenes0", new SerialisableScene(9, "no", "no", effects, false), error));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + (deviceId + 1) + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"3\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": " + null + "}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\":\"name\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }], \"shared\": \"false\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // GET all
        assertThrows(NullPointerException.class, () -> sceneC.getAllScenes(null, null));
        assertThrows(NullPointerException.class, () -> sceneC.getAllScenes("ScenesST", null));
        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes0")
                .header("session-token", "WrongScenesST"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // GET by id
        int sceneId = userService.getByMail("scenes0@smarthut.xyz").get().getScenes().get(0).getId();
        assertThrows(NullPointerException.class, () -> sceneC.getSceneById(null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.getSceneById(9, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.getSceneById(9, "ScenesST", null));
        this.mockmvc.perform(get("/scenes/" + sceneId)
                .header("user", "Scenes0")
                .header("session-token", "WrongScenesST"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(get("/scenes/" + (sceneId + 1))
                .header("user", "Scenes0")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // PUT scene
        assertThrows(NullPointerException.class, () -> sceneC.modifyScene(null, null, null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.modifyScene(sceneId, null, null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.modifyScene(sceneId, "ScenesST", null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.modifyScene(sceneId, "ScenesST", "Scenes0", null, null));
        assertThrows(BadRequestException.class, () ->
                sceneC.modifyScene(sceneId, "ScenesST", "Scenes0", new SerialisableScene(9, "no", "no", effects, false), error));
        this.mockmvc.perform(put("/scenes/" + (sceneId + 1))
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(put("/scenes/" + sceneId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes0")
                .content("{\"effects\": [{\"type\": \"3\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        this.mockmvc.perform(put("/scenes/" + sceneId)
//                .header("session-token", "ScenesST")
//                .header("user", "Scenes0")
//                .content("{\"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + (deviceId + 1) + "] }] }")
//                .contentType("application/json"))
//                .andDo(print())
//                .andExpect(status().is(400))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // PUT run
        assertThrows(NullPointerException.class, () -> sceneC.runScene(null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.runScene(sceneId, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.runScene(sceneId, "ScenesST", null));
        this.mockmvc.perform(put("/scenes/run/" + (sceneId + 1))
                .header("session-token", "ScenesST")
                .header("user", "Scenes0"))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // DELETE
        assertThrows(NullPointerException.class, () -> sceneC.deleteScene(null, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.deleteScene(sceneId, null, null));
        assertThrows(NullPointerException.class, () -> sceneC.deleteScene(sceneId, "ScenesST", null));
        this.mockmvc.perform(delete("/scenes/" + (sceneId + 1))
                .header("session-token", "ScenesST")
                .header("user", "Scenes0"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldSuccessfullyPostScene() throws Exception {
        User sceneUser1 = new User("scenes1@smarthut.xyz", "1234", "Scenes1", "Post Scene");
        sceneUser1.setVerified(true);
        sceneUser1.setSessionToken("ScenesST");
        userService.insert(sceneUser1);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes1")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes1");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes1")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes1")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldSuccessfullyPostAndGetScene() throws Exception {
        User sceneUser2 = new User("scenes2@smarthut.xyz", "1234", "Scenes2", "Post Scene");
        sceneUser2.setVerified(true);
        sceneUser2.setSessionToken("ScenesST");
        userService.insert(sceneUser2);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes2")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes2");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes2")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes2")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int scenesId = userService.getByMail("scenes2@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes2")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + scenesId)
                .header("user", "Scenes2")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldSuccessfullyPostAndPutScene() throws Exception {
        User sceneUser3 = new User("scenes3@smarthut.xyz", "1234", "Scenes3", "Post Scene");
        sceneUser3.setVerified(true);
        sceneUser3.setSessionToken("ScenesST");
        userService.insert(sceneUser3);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes3")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes3");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes3")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes3")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int scenesId = userService.getByMail("scenes3@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(put("/scenes/" + scenesId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes3")
                .content("{\"name\":\"anotherName\",\"icon\":\"/images/unknown-device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldSuccessfullyPostPutGetAndDeleteScene() throws Exception {
        User sceneUser4 = new User("scenes4@smarthut.xyz", "1234", "Scenes4", "Post Scene");
        sceneUser4.setVerified(true);
        sceneUser4.setSessionToken("ScenesST");
        userService.insert(sceneUser4);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes4")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes4");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes4")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes4")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }], \"shared\": \"false\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int scenesId = userService.getByMail("scenes4@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + scenesId)
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(put("/scenes/" + scenesId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes4")
                .content("{\"name\":\"anotherName\",\"icon\":\"/images/unknown-device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }], \"shared\": \"true\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + scenesId)
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(delete("/scenes/" + scenesId)
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(204));

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + scenesId)
                .header("user", "Scenes4")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void shouldSuccessfullyPostAndPutAllTypesOfScenes() throws Exception {
        User sceneUser5 = new User("scenes5@smarthut.xyz", "1234", "Scenes5", "Post Scene");
        sceneUser5.setVerified(true);
        sceneUser5.setSessionToken("ScenesST");
        userService.insert(sceneUser5);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes5");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic-device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic-device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"Thermostat\",\"icon\":\"/images/generic-device\", \"type\":\"11\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"Light\",\"icon\":\"/images/generic-device\", \"type\":\"1\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"SmartCurtains\",\"icon\":\"/images/generic_device\", \"type\":\"12\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int dimmableLight1Id = devices.get(0).getId();
        int dimmableLight2Id = devices.get(1).getId();
        int thermostatId = devices.get(2).getId();
        int lightId = devices.get(3).getId();
        int curtainsId = devices.get(4).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic-device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + dimmableLight1Id + ", " + dimmableLight2Id + "] }], \"shared\": \"true\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name2\",\"icon\":\"/images/generic-device\", \"effects\": [{\"type\": \"2\", \"name\": \"name2\", \"slider\": \"5.0\", \"devices\": [" + thermostatId + "] }], \"shared\": \"true\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name3\",\"icon\":\"/images/generic-device\", \"effects\": [{\"type\": \"3\", \"name\": \"name3\", \"on\": \"true\", \"devices\": [" + lightId + "] }], \"shared\": \"true\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name4\",\"icon\":\"/images/generic-device\", \"effects\": [{\"type\": \"4\", \"name\": \"name4\", \"slider\": \"1.0\", \"devices\": [" + curtainsId + "] }], \"shared\": \"true\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int dimmableId = userService.getByMail("scenes5@smarthut.xyz").get().getScenes().get(0).getId();
        int temperatureId = userService.getByMail("scenes5@smarthut.xyz").get().getScenes().get(1).getId();
        int powerId = userService.getByMail("scenes5@smarthut.xyz").get().getScenes().get(2).getId();
        int apertureId = userService.getByMail("scenes5@smarthut.xyz").get().getScenes().get(3).getId();

        this.mockmvc.perform(put("/scenes/" + dimmableId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"shared\": \"false\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(put("/scenes/" + temperatureId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name2b\",\"icon\":\"/images/unknown-device\", \"effects\": [{\"type\": \"2\", \"name\": \"name2b\", \"slider\": \"4.5\", \"devices\": [" + thermostatId + "] }], \"shared\": \"false\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(put("/scenes/" + powerId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name3b\",\"icon\":\"/images/unknown-device\", \"effects\": [{\"type\": \"3\", \"name\": \"name3b\", \"on\": \"false\", \"devices\": [" + lightId + "] }], \"shared\": \"false\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(put("/scenes/" + apertureId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes5")
                .content("{\"name\":\"name4b\",\"icon\":\"/images/unknown-device\", \"effects\": [{\"type\": \"4\", \"name\": \"name4b\", \"slider\": \"0.0\", \"devices\": [" + curtainsId + "] }], \"shared\": \"false\" }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldSuccessfullyPostAndRunScene() throws Exception {
        User sceneUser6 = new User("scenes6@smarthut.xyz", "1234", "Scenes6", "Post Scene");
        sceneUser6.setVerified(true);
        sceneUser6.setSessionToken("ScenesST");
        userService.insert(sceneUser6);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes6")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes6");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes6")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int deviceId = devices.get(0).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes6")
                .content("{\"name\":\"name\",\"icon\":\"/images/generic_device\", \"effects\": [{\"type\": \"1\", \"name\": \"name\", \"slider\": \"0.5\", \"devices\": [" + deviceId + "] }] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int sceneId = userService.getByMail("scenes6@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(put("/scenes/run/" + sceneId)
                .header("user", "Scenes6")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    void shouldSuccessfullyPostPutGetRunAndDeleteAllTypesOfScenes() throws Exception {
        User sceneUser7 = new User("scenes7@smarthut.xyz", "1234", "Scenes7", "Post Scene");
        sceneUser7.setVerified(true);
        sceneUser7.setSessionToken("ScenesST");
        userService.insert(sceneUser7);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Room> rooms = userService.getPopulatedRooms("Scenes7");
        Integer roomId = rooms.get(0).getId();

        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic_device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"DimmableLight\",\"icon\":\"/images/generic-device\", \"type\":\"2\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"Thermostat\",\"icon\":\"/images/generic-device\", \"type\":\"11\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"Thermostat\",\"icon\":\"/images/generic-device\", \"type\":\"11\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"Light\",\"icon\":\"/images/generic-device\", \"type\":\"1\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"Light\",\"icon\":\"/images/generic-device\", \"type\":\"1\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"SmartCurtains\",\"icon\":\"/images/generic_device\", \"type\":\"12\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockmvc.perform(post("/devices/")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"SmartCurtains\",\"icon\":\"/images/generic_device\", \"type\":\"12\",\"roomId\":\"" + roomId + "\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Device> devices = rooms.get(0).getDevices();
        int dimmableLight1Id = devices.get(0).getId();
        int dimmableLight2Id = devices.get(1).getId();
        int thermostat1Id = devices.get(2).getId();
        int thermostat2Id = devices.get(3).getId();
        int light1Id = devices.get(4).getId();
        int light2Id = devices.get(5).getId();
        int curtains1Id = devices.get(6).getId();
        int curtains2Id = devices.get(7).getId();

        this.mockmvc.perform(post("/scenes")
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"nameA\",\"icon\":\"/images/generic_device\", " +
                        "\"effects\": [{\"type\": \"1\", \"name\": \"nameB\", \"slider\": \"0.5\", \"devices\": [" + dimmableLight1Id + ", " + dimmableLight2Id + "]}, " +
                        "{\"type\": \"2\", \"name\": \"nameC\", \"slider\": \"10.0\", \"devices\": [" + thermostat1Id + ", " + thermostat2Id + "]}, " +
                        "{\"type\": \"3\", \"name\": \"nameD\", \"on\": \"false\", \"devices\": [" + light1Id + ", " + light2Id + "]}, " +
                        "{\"type\": \"4\", \"name\": \"nameE\", \"slider\": \"1.0\", \"devices\": [" + curtains1Id + ", " + curtains2Id + "]}" +
                        "], \"shared\": \"false\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int sceneId = userService.getByMail("scenes7@smarthut.xyz").get().getScenes().get(0).getId();

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(put("/scenes/run/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(204));

        this.mockmvc.perform(put("/scenes/" + sceneId)
                .header("session-token", "ScenesST")
                .header("user", "Scenes7")
                .content("{\"name\":\"nameA2\", " +
                        "\"effects\": [{\"type\": \"1\", \"name\": \"nameB\", \"slider\": \"0.5\", \"devices\": [" + dimmableLight1Id + ", " + dimmableLight2Id + "]}, " +
                        "{\"type\": \"3\", \"name\": \"nameD\", \"on\": \"true\", \"devices\": [" + light1Id + ", " + light2Id + "]}" +
                        "], \"shared\": \"false\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(put("/scenes/run/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(204));

        this.mockmvc.perform(delete("/scenes/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(204));

        this.mockmvc.perform(put("/scenes/run/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(404));

        this.mockmvc.perform(get("/scenes/")
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/scenes/" + sceneId)
                .header("user", "Scenes7")
                .header("session-token", "ScenesST"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}
