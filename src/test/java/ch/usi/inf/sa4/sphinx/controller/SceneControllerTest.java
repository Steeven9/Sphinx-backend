package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
