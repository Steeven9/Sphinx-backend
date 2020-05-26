package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.AutomationService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.RoomStorage;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
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
    @Autowired
    private AutomationService automationService;

    @BeforeAll
    @DirtiesContext
    void wipe() {
    }

    @BeforeEach
    void setUp() {
        User newUser = new User("test@mail.com", "1234", username, "mario rossi");
        userService.insert(newUser);
        user = userService.get(username).get();
        user.addRoom(new Room());
        userService.update(user);
        user = userService.get(user.getUsername()).get();

    }

    @AfterEach
    void clean() {
        userService.delete(username);
    }



    @Test
    @Disabled
    void shouldReturn400WithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(get("/devices/test")).andDo(print())
                .andExpect(status().is(400));
    }


    @Test
    @Disabled
    void shouldNotCreateWithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(post("/automations/")).andDo(print())
                .andExpect(status().is(400));
        this.mockmvc.perform(post("/automations/")
                .header("user", "")).andDo(print()).andExpect(status().is(400));
    }

    @Test
    @Disabled
    void shouldNotEditWithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(put("/automations/test")).andDo(print())
                .andExpect(status().is(400));
        this.mockmvc.perform(put("/automations/test")
                .header("user", "")).andDo(print()).andExpect(status().is(400));
    }

}
