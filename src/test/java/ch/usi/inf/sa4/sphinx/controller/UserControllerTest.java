package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.service.UserService;

import ch.usi.inf.sa4.sphinx.service.UserStorage;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Disabled;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockmvc;
    @Autowired
    private UserService userService;
    @Autowired
    private DummyDataAdder dummyDataAdder;

    @BeforeAll
    void init() {
        dummyDataAdder.addDummyData();
    }

    @Test
    public void shouldGet401FromWrongUsername() throws Exception {
        this.mockmvc.perform(get("/user/test").header("session-token", "banana"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldSuccessfullyGetUserWithValidData() throws Exception {
        this.mockmvc.perform(get("/user/user1").header("session-token", "user1SessionToken"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void shouldNotCreateUserWithoutData() throws Exception {
        this.mockmvc.perform(post("/user/test"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithAlreadyTakenData() throws Exception {
        this.mockmvc.perform(post("/user/user1").content(
                "{\"email\": \"unv@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"user1\"}"
        )
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithIncompleteOrInconsistentData() throws Exception {
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
    }

    @Test
    public void shouldReturn400FromNoSessionTokenGet() throws Exception {
        this.mockmvc.perform(get("/user/test"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturn401FromInvalidSessionTokenGet() throws Exception {
        this.mockmvc.perform(get("/user/test")
                .header("session-token", "user2SessionToken"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyCreateUserOnValidPost() throws Exception {
        this.mockmvc.perform(post("/user/test")
                .content("{\"email\": \"test@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturn400FromNoSessionTokenPut() throws Exception {
        this.mockmvc.perform(put("/user/user1")
                .content("{\"email\": \"test@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturn401FromInvalidSessionTokenPut() throws Exception {
        this.mockmvc.perform(put("/user/user1")
                .header("session-token", "user2SessionToken")
                .content("{\"email\": \"test@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyModifyUserOnValidPut() throws Exception {
        this.mockmvc.perform(put("/user/user2")
                .header("session-token", "user2SessionToken")
                .content("{\"email\": \"test2@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test2\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));

        // To test no modifications
        this.mockmvc.perform(put("/user/test2")
                .header("session-token", "user2SessionToken")
                .content("{\"email\": \"test2@smarthut.xyz\", \"fullname\": \"Marco Tereh\", \"password\": \"12345\", \"username\": \"test2\"}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void shouldReturn400FromNoSessionTokenDelete() throws Exception {
        this.mockmvc.perform(delete("/user/pellicangelo"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturn401FromInvalidSessionTokenDelete() throws Exception {
        this.mockmvc.perform(delete("/user/pellicangelo").header("session-token", "sessionToken"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyDeleteUserOnValidData() throws Exception {
        this.mockmvc.perform(delete("/user/emptyUser").header("session-token", "emptyUserSessionToken"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void shouldSuccessfullyNotModifyUserOnEmptyPut() throws Exception {
        this.mockmvc.perform(put("/user/user1")
                .header("session-token", "user1SessionToken")
                .content("{\"email\": " + null + ", \"fullname\": " + null + ", \"password\": " + null + ", \"username\": " + null + "}")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void updateUserWithErrors() {
        UserController userC = new UserController();
        SerialisableUser user = new SerialisableUser();
        BindException error = new BindException(new Object(), "something");
        error.addError(new ObjectError("something", "something"));
        assertThrows(BadRequestException.class, () -> userC.updateUser("blah", user, "something", error));
    }
}