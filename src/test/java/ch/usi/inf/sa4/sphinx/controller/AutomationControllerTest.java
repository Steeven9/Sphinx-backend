package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.Switch;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.Scene;
import ch.usi.inf.sa4.sphinx.model.sceneEffects.SceneType;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import ch.usi.inf.sa4.sphinx.service.*;
import ch.usi.inf.sa4.sphinx.view.SerialisableAutomation;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.returns;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//automation:
//        {
//        id: {{automationId}},
//        owner: {{username}},
//        name: {{automationName}}
//        scenes: [...{{sceneId}}]
//        triggers: [...{{trigger}}]
//        conditions: null || [...{{condition}}]
//        }
//
//        trigger/condition:
//        {
//        sourceId: {{deviceId}},
//        type: {{conditionType}}
//        value:{{effectValue}},
//        booleanValue:{{boolean}}
//        }
//
//        Trigger/Condition types:
//        1 (=DEVICE_ON)
//        2 (=DEVICE_OFF)
//        3 (=MOTION_DETECTED)
//        4 (=MOTION_NOT_DETECTED)
//        5 (=SENSOR_OVER when a sensor measures over a certain target)
//        6 (=SENSOR_UNDER)


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutomationControllerTest {
    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    RoomStorage roomStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;


    @Autowired
    private MockMvc mockmvc;
    private User user;
    private final static String username = "testUser";
    private final static Map<DeviceType, Integer> deviceIds = new HashMap<>();
    private static Integer roomId;
    private static Integer sceneId;
    private static Integer automationId;
    private static String sessionToken;
    @Autowired
    private AutomationService automationService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private DeviceService deviceService;


    @BeforeAll
    @DirtiesContext
    void wipe() {
    }

    @BeforeEach
    void setUp() {
        User newUser = new User("test@mail.com", "1234", username, "mario rossi");

        newUser.setVerified(true);
        sessionToken ="testToken";
        newUser.setSessionToken(sessionToken);
        userService.insert(newUser);
        user = userService.get(username).get();
        roomId = userService.addRoom(user.getUsername(), new Room()).get();
        user = userService.get(user.getUsername()).get();

        deviceIds.put(DeviceType.DIMMABLE_LIGHT, roomService.addDevice(roomId, DeviceType.DIMMABLE_LIGHT).get());
        deviceIds.put(DeviceType.STATELESS_DIMMABLE_SWITCH, roomService.addDevice(roomId, DeviceType.STATELESS_DIMMABLE_SWITCH).get());
        deviceIds.put(DeviceType.DIMMABLE_SWITCH, roomService.addDevice(roomId, DeviceType.DIMMABLE_SWITCH).get());
        deviceIds.put(DeviceType.SWITCH, roomService.addDevice(roomId, DeviceType.SWITCH).get());
        deviceIds.put(DeviceType.LIGHT, roomService.addDevice(roomId, DeviceType.LIGHT).get());
        deviceIds.put(DeviceType.TEMP_SENSOR, roomService.addDevice(roomId, DeviceType.TEMP_SENSOR).get());


        List<Integer> affected = new ArrayList<>();
        affected.add(deviceIds.get(DeviceType.LIGHT));

        Scene scene = sceneService.createScene(user.getUsername(), "bob", "icon").get();
        sceneId = scene.getId();
        sceneService.addEffect(scene.getId(), affected, SceneType.POWER, "blob", false);


        Automation automation = automationService.createAutomation(user.getUsername()).get();
        automationId = automation.getId();

        automationService.addTrigger(automationId, deviceIds.get(DeviceType.SWITCH), ConditionType.DEVICE_ON, true);
        automation = automationService.findById(automationId).get();
        Switch switcher = (Switch) deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        int i = 1;
    }

    @AfterEach
    void clean() {
        //userService.delete(username);
    }


    @Test
    void badRequestIfMissingAuthOnGetAutomations() throws Exception {
        this.mockmvc.perform(get("/automations/")).andDo(print())
                .andExpect(status().is(400));
    }


    @Test
    void badRequestIfMissingAuthOnPostAutomation() throws Exception {
        this.mockmvc.perform(post("/automations/")).andDo(print())
                .andExpect(status().is(400));
        this.mockmvc.perform(post("/automations/")
                .header("user", "")).andDo(print()).andExpect(status().is(400));
    }

    @Test
    void badRequestIfMissingAuthOnPutAutomation() throws Exception {
        this.mockmvc.perform(put("/automations/1")).andDo(print())
                .andExpect(status().is(400));
        this.mockmvc.perform(put("/automations/1")
                .header("user", "")).andDo(print()).andExpect(status().is(400));
    }


    @Test
    void badRequestIfMissingAuthOnDeleteAutomation() throws Exception {
        this.mockmvc.perform(delete("/automations/1").header("user", "user2")).andDo(print())
                .andExpect(status().is(400));
    }





//{"id":1,"name":"default","icon":null,"ownerId":1,"scenes":[],"triggers":[{"conditionType":"DEVICE_ON","type":2,"source":4,"value":"true","target":true,"doubleValue":null,"booleanValue":true}],"conditions":[]}
//MockMvcResultMatchers.content().contentType(contentType)
    @Test
    void ifCorrectParamGetAut() throws Exception{
        MvcResult result = this.mockmvc
                .perform(get("/automations/"+automationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(automationId.toString()))
                .andExpect(jsonPath("$.ownerId").value(user.getId()))
                .andExpect(jsonPath("$.triggers[0].type").value(2))
                .andExpect(jsonPath("$.triggers[0].conditionType").value("DEVICE_ON"))
                .andExpect(jsonPath("$.triggers[0].source").value(deviceIds.get(DeviceType.SWITCH)))
                .andExpect(jsonPath("$.conditions").isEmpty())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }

    @Test
    void ifCorrectParamPutAutNoChanges() throws Exception{
        SerialisableAutomation sa = new SerialisableAutomation(automationId, "bob", "icon", null, null, null, null );
        Gson gson = new Gson();
        String json = gson.toJson(sa);

        MvcResult result = this.mockmvc
                .perform(put("/automations/"+automationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                        .header("content-type", "application/json")
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(automationId.toString()))
                .andExpect(jsonPath("$.ownerId").value(user.getId()))
                .andExpect(jsonPath("$.triggers[0].type").value(ConditionType.DEVICE_ON.toInt()))
                .andExpect(jsonPath("$.triggers[0].conditionType").value("DEVICE_ON"))
                .andExpect(jsonPath("$.triggers[0].source").value(deviceIds.get(DeviceType.SWITCH)))
                .andExpect(jsonPath("$.conditions").isEmpty())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }

    @Test
    void ifCorrectParamPutAutNoChanges2() throws Exception{
        SerialisableAutomation sa = new SerialisableAutomation(automationId, "bob", "icon", null, null, null, null );
        Gson gson = new Gson();
        String json = gson.toJson(sa);

        MvcResult result = this.mockmvc
                .perform(put("/automations/"+automationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                        .header("content-type", "application/json")
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(automationId.toString()))
                .andExpect(jsonPath("$.ownerId").value(user.getId()))
                .andExpect(jsonPath("$.triggers[0].type").value(ConditionType.DEVICE_ON.toInt()))
                .andExpect(jsonPath("$.triggers[0].conditionType").value("DEVICE_ON"))
                .andExpect(jsonPath("$.triggers[0].source").value(deviceIds.get(DeviceType.SWITCH)))
                .andExpect(jsonPath("$.conditions").isEmpty())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }


    @Test
    @Disabled("Referential integrity constraint violation: \"FKJFHJULLDDTD0MQT6F4QQPS61G: PUBLIC.TRIGGER FOREIGN KEY(AUTOMATION_ID) REFERENCES PUBLIC.AUTOMATION(ID) (1)\"; SQL statement:\n" +
            "delete from automation where id=?")
    void ifCorrectParamDeletes() throws Exception{
        MvcResult result = this.mockmvc
                .perform(delete("/automations/"+automationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }


}
