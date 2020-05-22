package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockmvc;
    @Autowired
    private DummyDataAdder dummyDataAdder;

    @BeforeAll
    void init() throws Exception {
        dummyDataAdder.addDummyData();
        // add guest user1 to user2
        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("user1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));
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
    public void shouldSuccessfullyReturnGuestsEmpty() throws Exception {
        this.mockmvc.perform(get("/guests")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldSuccessfullyPostAndReturnGuests() throws Exception {
        this.mockmvc.perform(post("/guests/")
                .header("session-token", "user2SessionToken")
                .header("user", "user2")
                .content("user1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(201));

        this.mockmvc.perform(get("/guests")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Disabled("401")
    @Test
    public void shouldSuccessfullyReturnHousesAccess() throws Exception {
        this.mockmvc.perform(get("/guests/houses")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Disabled("401")
    @Test
    public void shouldSuccessfullyDeleteGuest() throws Exception {
        this.mockmvc.perform(delete("/guests/guest2")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Disabled("401")
    @Test
    public void shouldSuccessfullyGetGuestDevices() throws Exception {
        this.mockmvc.perform(get("/guests/user2/devices/guest1")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

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
