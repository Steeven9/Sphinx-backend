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
import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import ch.usi.inf.sa4.sphinx.misc.BadRequestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.returns;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;


//automation:
//        {
//        id: {{onAutomationId}},
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
    private static Integer onAutomationId;
    private static Integer sensorAutomationId;
    private static Integer motionAutomationId;
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
        deviceIds.put(DeviceType.MOTION_SENSOR, roomService.addDevice(roomId, DeviceType.MOTION_SENSOR).get());


        List<Integer> affected = new ArrayList<>();
        affected.add(deviceIds.get(DeviceType.LIGHT));

        Scene scene = sceneService.createScene(user.getUsername(), "bob", "icon").get();
        sceneId = scene.getId();
        sceneService.addEffect(scene.getId(), affected, SceneType.POWER, "blob", false);


        Automation automation = automationService.createAutomation(user.getUsername()).get();
        onAutomationId = automation.getId();
        sensorAutomationId = automationService.createAutomation(user.getUsername()).get().getId();
        motionAutomationId = automationService.createAutomation(user.getUsername()).get().getId();


        automationService.addTrigger(onAutomationId, deviceIds.get(DeviceType.SWITCH), ConditionType.DEVICE_ON, true);
        automationService.addTrigger(sensorAutomationId, deviceIds.get(DeviceType.TEMP_SENSOR), ConditionType.SENSOR_OVER, 10.0);
        automationService.addTrigger(motionAutomationId, deviceIds.get(DeviceType.MOTION_SENSOR), ConditionType.MOTION_DETECTED, true);

        automation = automationService.findById(onAutomationId).get();
        Switch switcher = (Switch) deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        int i = 1;
    }

//    @AfterEach
    void clean() {
        userService.delete(username);
    }

    private void  deleteAll(){
        automationService.deleteAllByUsername(username);
    }

    @Test
    @Disabled
    void testTest(){
        deleteAll();
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
    @Disabled
    void ifCorrectParamGetAut() throws Exception{
        MvcResult result = this.mockmvc
                .perform(get("/automations/"+ onAutomationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(onAutomationId.toString()))
                .andExpect(jsonPath("$.ownerId").value(user.getId()))
                .andExpect(jsonPath("$.triggers[0].conditionType").value(ConditionType.DEVICE_ON.toInt()))
                .andExpect(jsonPath("$.triggers[0].sourceId").value(deviceIds.get(DeviceType.SWITCH)))
                .andExpect(jsonPath("$.conditions").isEmpty())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }

    @Test
    void ifCorrectParamPutAutNoChanges() throws Exception{
        SerialisableAutomation sa = new SerialisableAutomation(onAutomationId, "bob", "icon", null, null, null, null );
        Gson gson = new Gson();
        String json = gson.toJson(sa);

        MvcResult result = this.mockmvc
                .perform(put("/automations/"+ onAutomationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                        .header("content-type", "application/json")
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(onAutomationId.toString()))
                .andExpect(jsonPath("$.ownerId").value(user.getId()))
                .andExpect(jsonPath("$.triggers[0].conditionType").value(ConditionType.DEVICE_ON.toInt()))
                .andExpect(jsonPath("$.triggers[0].sourceId").value(deviceIds.get(DeviceType.SWITCH)))
                .andExpect(jsonPath("$.conditions").isEmpty())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }

    @Test
    void ifCorrectParamPutAut() throws Exception{
        List<SerialisableCondition> triggers = new ArrayList<>();
        List<SerialisableCondition> conditions = new ArrayList<>();
        List<Integer> scenes = new ArrayList<>();
        scenes.add(sceneId);
        SerialisableCondition t1 = new SerialisableCondition(ConditionType.MOTION_DETECTED, deviceIds.get(DeviceType.MOTION_SENSOR), "true");
        SerialisableCondition c1 = new SerialisableCondition(ConditionType.DEVICE_ON, deviceIds.get(DeviceType.LIGHT), "true");
        triggers.add(t1);
        conditions.add(c1);
        SerialisableAutomation sa = new SerialisableAutomation(onAutomationId, "bob", "icon", null, scenes, triggers, conditions);
        Gson gson = new Gson();
        String json = gson.toJson(sa);

        MvcResult result = this.mockmvc
                .perform(put("/automations/"+ onAutomationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                        .header("content-type", "application/json")
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(jsonPath("$.id").value(onAutomationId.toString()))
//                .andExpect(jsonPath("$.ownerId").value(user.getId()))
//                .andExpect(jsonPath("$.triggers[0].type").value(ConditionType.DEVICE_ON.toInt()))
//                .andExpect(jsonPath("$.triggers[0].conditionType").value("DEVICE_ON"))
//                .andExpect(jsonPath("$.triggers[0].source").value(deviceIds.get(DeviceType.SWITCH)))
//                .andExpect(jsonPath("$.conditions").isEmpty())
//                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }


    @Test
    void testPost() throws Exception{
        List<SerialisableCondition> triggers = new ArrayList<>();
        List<Integer> scenes = new ArrayList<>();
        scenes.add(sceneId);
        SerialisableCondition s1 = new SerialisableCondition(ConditionType.MOTION_DETECTED, deviceIds.get(DeviceType.MOTION_SENSOR), "true");
        triggers.add(s1);

        SerialisableAutomation sa = new SerialisableAutomation(onAutomationId, "bob", "icon", user.getId(), scenes, triggers, null );
        Gson gson = new Gson();
        String json = gson.toJson(sa);

        MvcResult result = this.mockmvc
                .perform(post("/automations/")
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                        .header("content-type", "application/json")
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.ownerId").value(user.getId()))
                .andExpect(jsonPath("$.triggers[0].conditionType").value(ConditionType.MOTION_DETECTED.toInt()))
                .andExpect(jsonPath("$.triggers[0].sourceId").value(deviceIds.get(DeviceType.MOTION_SENSOR)))
                .andExpect(jsonPath("$.conditions").isEmpty())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();
        return;

    }


    @Test
    @Disabled("Referential integrity constraint violation: \"FKJFHJULLDDTD0MQT6F4QQPS61G: PUBLIC.TRIGGER FOREIGN KEY(AUTOMATION_ID) REFERENCES PUBLIC.AUTOMATION(ID) (1)\"; SQL statement:\n" +
            "delete from automation where id=?")
    void ifCorrectParamDeletes() throws Exception{
        MvcResult result = this.mockmvc
                .perform(delete("/automations/"+ onAutomationId)
                        .header("user", user.getUsername())
                        .header("session-token", sessionToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String res = result.getResponse().getContentAsString();//Useful to debug
        return;
    }

    @Test
    void testNullPointers() {
        SerialisableAutomation sa = new SerialisableAutomation();
        AutomationController ac = new AutomationController();
        BindException error = new BindException(new Object(), "something");
        error.addError(new ObjectError("something", "something"));
        assertThrows(BadRequestException.class, () -> ac.createAutomation("AST", "Auto1", sa, error));
        assertThrows(NullPointerException.class, () -> ac.createAutomation("AST", "Auto1", sa, null));
        assertThrows(NullPointerException.class, () -> ac.createAutomation("AST", "Auto1", null, null));
        assertThrows(NullPointerException.class, () -> ac.createAutomation("AST", null, null, null));
        assertThrows(NullPointerException.class, () -> ac.createAutomation(null, null, null, null));
        assertThrows(BadRequestException.class, () -> ac.modifyAutomation(0, "AST", "Auto1", sa, error));
        assertThrows(NullPointerException.class, () -> ac.modifyAutomation(0,"AST", "Auto1", sa, null));
        assertThrows(NullPointerException.class, () -> ac.modifyAutomation(0,"AST", "Auto1", null, null));
        assertThrows(NullPointerException.class, () -> ac.modifyAutomation(0,"AST", null, null, null));
        assertThrows(NullPointerException.class, () -> ac.modifyAutomation(0,null, null, null, null));
        assertThrows(NullPointerException.class, () -> ac.modifyAutomation(null,null, null, null, null));
        assertThrows(NullPointerException.class, () -> ac.deleteAutomation("AST", "Auto1", null));
        assertThrows(NullPointerException.class, () -> ac.deleteAutomation("AST", null, null));
        assertThrows(NullPointerException.class, () -> ac.deleteAutomation(null, null, null));
        assertThrows(NullPointerException.class, () -> ac.getAutomations("AST", null));
        assertThrows(NullPointerException.class, () -> ac.getAutomations(null, null));
        assertThrows(NullPointerException.class, () -> ac.getAutomation("AST", "Auto1", null));
        assertThrows(NullPointerException.class, () -> ac.getAutomation("AST", null, null));
        assertThrows(NullPointerException.class, () -> ac.getAutomation(null, null, null));
    }



}
