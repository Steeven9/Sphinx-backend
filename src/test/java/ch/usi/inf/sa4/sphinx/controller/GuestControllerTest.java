package ch.usi.inf.sa4.sphinx.controller;

import org.junit.jupiter.api.Test;
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
public class GuestControllerTest {

    @Autowired
    private MockMvc mockmvc;


    @Disabled("Not Implemented")
    @Test
    public void shouldGet400FromNoToken() throws Exception {
        this.mockmvc.perform(get("/guests/").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(get("/guests/houses").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username}").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(post("/guests/").header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

        this.mockmvc.perform(delete(("/guests/{guest_username}")).header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(400));

//        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username").header("user", "user2"))
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

        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username}")
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(post("/guests/")
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(delete(("/guests/{guest_username}"))
                .header("session-token", "banana")
                .header("user", "user2"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username")
//        .header("session-token", "banana")
//        .header("user", "user2"))
//                .andDo(print())
//                .andExpect(status().is(401));
    }
    @Disabled("Not Implemented")
    @Test
    public void shouldGet401FromWrongUser() throws Exception {

        this.mockmvc.perform(get("/guests/")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/houses")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username}")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(post("/guests/")
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

        this.mockmvc.perform(delete(("/guests/{guest_username}"))
                .header("user", "fakeUser"))
                .andDo(print())
                .andExpect(status().is(401));

//        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username")
//        .header("user", "fakeUser"))
//                .andDo(print())
//                .andExpect(status().is(401));
    }

    @Disabled("Not Implemented")
    @Test
    public void shouldSuccessfullyReturnGuests() throws Exception {
        this.mockmvc.perform(get("/guests").header("user", "user2").header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Disabled("Not Implemented")
    @Test
    public void shouldSuccessfullyReturnHousesAccess() throws Exception {
        this.mockmvc.perform(get("/guests/houses").header("user", "user2").header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Disabled("Not Implemented")
    @Test
    public void shouldSuccessfullyDeleteGuest() throws Exception {
        this.mockmvc.perform(delete("/guests/{guest_username}").header("user", "user2").header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Disabled("Not Implemented")
    @Test
    public void shouldSuccessfullyCreateGuest() throws Exception {
        this.mockmvc.perform(post("/guests/").header("user", "user2").header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Disabled("Not Implemented")
    @Test
    public void shouldSuccessfullyGetGuestDevices() throws Exception {
        this.mockmvc.perform(get("/guests/{username}/devices/{guest_username}").header("user", "user2").header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


//    @Disabled("Not Implemented")
//    @Test
//    public void shouldSuccessfullyGetGuestScenes() throws Exception {
//        this.mockmvc.perform(get("/guests/{username}/scenes/{guest_username}").header("user", "user2").header("session-token", "banana"))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }



}
