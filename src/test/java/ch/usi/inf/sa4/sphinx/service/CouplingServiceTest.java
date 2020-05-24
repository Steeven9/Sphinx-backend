package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.RoomStorage;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest
public class CouplingServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    RoomStorage roomStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;
    private User user;
    private final static String username = "testUser";

    @BeforeAll
    @DirtiesContext
    void wipe() {
    }

    @BeforeEach
    void setUp() {
        User newUser = new User("test@mail.com", "1234", username, "mario rossi");
        userService.insert(newUser);
        user = userService.get(username).get();
        userService.addRoom(username, new Room());
        //int roomId = user.g

    }

    @AfterEach
    void clean() {
        userService.delete(username);
    }








}
