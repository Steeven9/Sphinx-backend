package ch.usi.inf.sa4.sphinx.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Disabled;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @Test
    public void shouldGet404FromWrongUsername() throws Exception {
        this.mockmvc.perform(get("/user/test").header("session-token", "banana"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotCreateUserWithoutData() throws Exception {
        this.mockmvc.perform(post("/user/test"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithIncompleteOrInconsistentData() throws Exception {
        this.mockmvc.perform(post("/user/test")
                    .content(
                    "{\"email\": \"test@usi.ch\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\"}"
                    )
                    .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated());
        this.mockmvc.perform(post("/user/test").content(
                "{\"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test\"}"
        )
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        this.mockmvc.perform(post("/user/test").content(
                "{\"email\": \"test@usi.ch\", \"password\": \"12345\", \"username\": \"test\"}"
        )
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        this.mockmvc.perform(post("/user/test").content(
                "{\"email\": \"test@usi.ch\", \"fullname\": \"Marco Tereh\", \"username\": \"test\"}"
        )
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        this.mockmvc.perform(post("/user/test").content(
                "{\"email\": \"test@usi.ch\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"nottest\"}"
        )
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGet400FromNoSessionToken() throws Exception {
        this.mockmvc.perform(get("/user/test"))
                .andDo(print())
                .andExpect(status().is(400));
    }

	//@Disabled("Not implemented")
    @Test
    public void shouldSuccessfullyCreateUserOnValidPost() throws Exception {
        this.mockmvc.perform(post("/user/test").content(
                "{\"email\": \"test@usi.ch\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test\"}"
        )
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}