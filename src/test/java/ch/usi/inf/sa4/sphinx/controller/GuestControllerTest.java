package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ch.usi.inf.sa4.sphinx.model.User;

import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Disabled;


@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockmvc;
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
        this.mockmvc.perform(get("/guests/user1/devices")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Disabled(value = "try to fix pipeline")
    public void shouldSuccessfullyGetGuestDevices() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("user1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/guests/user2/devices")
                .header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print())
                .andExpect(status().is(200));

    }

    @Disabled(value = "Waiting for scenes")
    @Test
    public void shouldGet401OnGetGuestScenesWithWrongGuest() throws Exception {
        this.mockmvc.perform(get("/guests/user1/scenes")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Disabled(value = "Waiting for scenes")
    @Test
    public void shouldSuccessfullyGetGuestScenes() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("user1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/guests/user2/scenes")
                .header("user", "user1")
                .header("session-token", "user1SessionToken"))
                .andDo(print())
                .andExpect(status().is(200));

    }


//    @Disabled("Not Implemented")
//    @Test
//    public void shouldSuccessfullyGetGuestScenes() throws Exception {
//        this.mockmvc.perform(get("/guests/user2/scenes/{guest_username}").header("user", "user2").header("session-token", "banana"))
//                .andDo(print())
//                .andExpect(status().is(200))
    //               .andExpect(content().contentType(MediaType.APPLICATION_JSON)
//    }



}
