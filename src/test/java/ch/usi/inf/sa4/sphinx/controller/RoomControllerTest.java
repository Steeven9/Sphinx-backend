package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;

import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private DummyDataAdder dummyDataAdder;
    @Autowired
    private UserService userService;


    @BeforeAll
    void init() {
        dummyDataAdder.addDummyData();
    }

    @Test
    void shouldReturn400WithNoTokenOrUser() throws Exception {
        this.mockmvc.perform(get("/rooms/test").header("user", ""))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(get("/rooms/test"))
                .andDo(print()).andExpect(status().is(400));


    }


    @Test
    void shouldNotPostWithNoSessionTokenOrWrongUser() throws Exception {
        this.mockmvc.perform(post("/rooms/").header("session-token", "")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(post("/rooms/").header("user", "")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));
    }

    @Test
    void shouldNotModifyWithNoSessionTokenOrWrongUser() throws Exception {

        this.mockmvc.perform(put("/rooms/roomId2/").header("user", "user2"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(put("/rooms/roomId1/"))
                .andDo(print()).andExpect(status().is(400));
    }

    @Test
    void shouldNotRemoveWithNoSessionTokenOrWrongUser() throws Exception {


        this.mockmvc.perform(delete("/rooms/test/").header("user", "anyone"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(delete("/rooms/test/"))
                .andDo(print()).andExpect(status().is(400));
    }

    @Test
    void shouldNotAcceptIncompleteOrInconsistentData() throws Exception {
        //post
        this.mockmvc.perform(post("/rooms/").header("user", "").content("{\"name\": \"\", \"icon\": \"livingRoom\", \"background\": \"livingRoom\" }")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));

        this.mockmvc.perform(post("/rooms/").header("user", "").content(" {\"icon\": \"livingRoom\", \"background\": \"livingRoom\"} ")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(post("/rooms/").header("user", "").content("{\"name\": \"Room1\", \"icon\": \"livingRoom\"} ")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(post("/rooms/").header("user", "").content("{\"name\": \"Room1\", \"background\": \"livingRoom\"} ")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(400));


        //put
        this.mockmvc.perform(put("/rooms/test/").content("{\"name\": \"\", \"icon\": \"livingRoom\", \"background\": \"livingRoom\"} ").contentType(("application/json")))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(put("/rooms/test/").content(" {\"icon\": \"livingRoom\", \"background\": \"livingRoom\"} ").contentType(("application/json")))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(put("/rooms/test/").content("{\"name\": \"Room1\", \"icon\": \"livingRoom\" }").contentType(("application/json")))
                .andDo(print()).andExpect(status().is(400));
        this.mockmvc.perform(put("/rooms/test/").content("{\"name\": \"Room1\", \"background\": \"livingRoom\"} ").contentType(("application/json")))
                .andDo(print()).andExpect(status().is(400));


    }

    @Test
    void shouldReturn401OnNotValidToken() throws Exception {

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "Token")
                .header("user", "user2")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/rooms/")
                .header("session-token", "Token")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/rooms/1/devices/")
                .header("session-token", "Token")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(put("/rooms/3/")
                .header("session-token", "Token")
                .header("user", "user2")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(delete("/rooms/3/")
                .header("session-token", "Token")
                .header("user", "user2")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/rooms/")
                .header("user", "")
                .header("session-token", "user2SessionToken"))
                .andDo(print()).andExpect(status().is(401));

    }


    @Test
    void shouldBeSuccessfulPostRoom() throws Exception {

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status()
                        .is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void shouldSuccessfullyGetOnValidData() throws Exception {
        Integer id = userService.getPopulatedRooms("user1").get(0).getId();

        this.mockmvc.perform(get("/rooms/")
                .header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print()).andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/rooms/" + id).header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print()).andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockmvc.perform(get("/rooms/" + id + "/devices/").header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print()).andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @ParameterizedTest
    @CsvSource({
            "randomName, /images/default_icon, /images/default_room",
            "name1, null, /images/default_room",
            "name2, /images/default_icon, null",
            "null, /images/default_icon, /images/default_room",
    })
    void shouldSuccessfullyPutOnValidData(String name, String icon, String background) throws Exception {
        Room room = userService.getPopulatedRooms("user2").get(2);

        this.mockmvc.perform(put("/rooms/" + room.getId())
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("{\"name\": \"" + name + "\", \"icon\": \"" + icon + "\", \"background\": \"" + background + "\", \"devices\":[] }")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void shouldSuccessfullyDeleteOnValidData() throws Exception {
        Integer roomId = userService.getPopulatedRooms("user2").get(0).getId();

        this.mockmvc.perform(delete("/rooms/" + roomId)
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print()).andExpect(status().is(204));

    }


    @Test
    void shouldReturn401WhenNoRoomFound() throws Exception {
        this.mockmvc.perform(get("/rooms/100/devices/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test

    void shouldSuccessfullyPutOnNullData() throws Exception {
        User nullPut = new User("NullPut@smarthut.xyz", "1234", "NullPut", "Null Put");
        nullPut.setVerified(true);
        nullPut.setSessionToken("NullST");
        userService.insert(nullPut);

        this.mockmvc.perform(post("/rooms/")
                .header("session-token", "NullST")
                .header("user", "NullPut")
                .content("{\"name\": \" newRoom \",  \" icon\" : \"/images/default_room\", \"background\": \"/images/default_icon\", \"devices\": [] }")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Integer roomId = userService.getPopulatedRooms("NullPut").get(0).getId();

        this.mockmvc.perform(put("/rooms/" + roomId)
                .header("user", "NullPut")
                .header("session-token", "NullST")
                .content("{\"name\": \"" + null + "\", \"icon\": \"" + null + "\", \"background\": \"" + null + "\", \"devices\":[] }")
                .contentType("application/json"))
                .andDo(print()).andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

}


