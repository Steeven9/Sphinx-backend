package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
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
    void  init() {
        dummyDataAdder.user2();
    }


    @Test
    public void shouldGet400FromNoToken() throws Exception {
        this.mockmvc.perform(get("/guests/").header("username", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(get("/guests/houses").header("username", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(get("/guests/user2/devices/guest3").header("username", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(post("/guests/").header("username", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(delete(("/guests/guest3")).header("username", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

//        this.mockmvc.perform(get("/guests/user2/scenes/guest3").header("username", "user2"))
//                .andDo(print())
//                .andExpect(status().is(400));
    }

    @Disabled("Not Implemented")
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

        this.mockmvc.perform(get("/guests/user2/devices/guest2")
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(post("/guests/")
//                .header("user", "user2")
//                .header("session-token", "banana"))
//                .andDo(print())
//                .andExpect(status().is(401));

        this.mockmvc.perform(delete(("/guests/guest1"))
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/user2/devices/guest3")
        .header("session-token", "banana")
        .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));
    }
    @Disabled("Not Implemented")
    @Test
    public void shouldGet401FromWrongUser() throws Exception {

        this.mockmvc.perform(get("/guests/")
                .header("user", "fakeUser")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/houses")
                .header("user", "fakeUser")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/user2/devices/guest1")
                .header("user", "fakeUser")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(post("/guests/")
//                .header("user", "fakeUser")
//                .header("session-token", "user2SessionToken"))
//                .andDo(print())
//                .andExpect(status().is(401));

        this.mockmvc.perform(delete(("/guests/guest2"))
                .header("user", "fakeUser")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username")
//        .header("user", "fakeUser")
//        .header("session-token", "user2SessionToken"))
//                .andDo(print())
//                .andExpect(status().is(401));
    }


    @Test
    public void shouldGet401FromNoGuest() throws Exception {


        this.mockmvc.perform(get("/guests/user2/devices/guest10")
                .header("user", "user2")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));



        this.mockmvc.perform(delete(("/guests/guest23"))
                .header("user", "user2r")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(get("/guests/user2/devices/guest59")
//        .header("user", "fakeUser")
//        .header("session-token", "user2SessionToken"))
//                .andDo(print())
//                .andExpect(status().is(401));
    }

    @Disabled("401")
    @Test
    public void shouldSuccessfullyReturnGuests() throws Exception {
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
    public void shouldSuccessfullyCreateGuest() throws Exception {



        this.mockmvc.perform(post("/guests/")
                .header("user", "user2")
                .header("session-token", "user2SessionToken")
                .content("{\"email\": \"mario@usi.ch\", \"fullname\": \"mariorossi\", \"password\":\"1234\", \"username\": \"user1\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(204))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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

