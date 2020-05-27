package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

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
    public void shouldGet401OnLoginWithWrongUsername() throws Exception {
        this.mockmvc.perform(post("/auth/login/test321")
                .content("gfgfgfgf")
                .header("content-type", "application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldGet401OnLoginWithWrongEmail() throws Exception {
        this.mockmvc.perform(post("/auth/login/wrong")
                .content("{email:\"test@email.io\"}")
                .header("content-type", "application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyValidateOnValidData() throws Exception {
        this.mockmvc.perform(post("/auth/validate")
                .header("session-token","user1SessionToken")
                .header("user","user1"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void shouldSuccessfullyLoginOnValidData() throws Exception {
        this.mockmvc.perform(post("/auth/login/user2")
                .header("session-token","user2SessionToken")
                .header("user","user2")
                .content("1234")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void shouldReturn403OnLoginWithUnverifiedUser() throws Exception{
        this.mockmvc.perform(post("/auth/login/unverifiedUser")
                .header("user","unverifiedUser")
                .content("1234")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void shouldReturn401OnLoginWithWrongPassword() throws Exception{
        this.mockmvc.perform(post("/auth/login/user2")
                .header("user","user2")
                .content("1332")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldReturn401WithMissingUsername() throws Exception{
        this.mockmvc.perform(post("/auth/login/user")
                .header("user","")
                .content("1332")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldReturn400OnVerifyUserWithVerifiedUser() throws Exception {
        this.mockmvc.perform(post("/auth/verify/mario@smarthut.xyz")
                .content("verificationCode")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturn403OnVerifyUserWithWrongVerificationCode() throws Exception {
        this.mockmvc.perform(post("/auth/verify/unv@smarthut.xyz")
                .content("verificationCode")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyVerifyUserOnValidData() throws Exception {
        User newUser = new User("cataclismio@smarthut.xyz", "1234", "userCata1", "Cataclismio");
        userService.insert(newUser);
        String verificationCode = newUser.getVerificationToken();

        this.mockmvc.perform(post("/auth/verify/cataclismio@smarthut.xyz")
                .content(verificationCode)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void shouldReturn401OnResetEmailWithWrongEmail() throws Exception {
        this.mockmvc.perform(post("/auth/reset/invalidEmail"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyResetEmailOnValidData() throws Exception {
        User newUser = new User("cataclismio2@smarthut.xyz", "1234", "userCata2", "Cataclismio2");
        userService.insert(newUser);

        this.mockmvc.perform(post("/auth/reset/cataclismio2@smarthut.xyz"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void shouldReturn400OnChangePasswordWithMissingNewPassword() throws Exception {
        User newUser = new User("cataclismio4@smarthut.xyz", "1234", "userCata4", "Cataclismio4");
        userService.insert(newUser);
        newUser.createResetCode();
        var resetCode = newUser.getResetCode();

        this.mockmvc.perform(post("/auth/reset/cataclismio4@smarthut.xyz/" + resetCode))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldReturn401OnChangePasswordWithIncorrectResetCode() throws Exception {
        User newUser = new User("cataclismio5@smarthut.xyz", "1234", "userCata5", "Cataclismio5");
        userService.insert(newUser);

        this.mockmvc.perform(post("/auth/reset/cataclismio5@smarthut.xyz/resetCode")
                .content("newPassword")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void shouldSuccessfullyChangePasswordOnValidData() throws Exception {
        User newUser = new User("cataclismio3@smarthut.xyz", "1234", "userCata3", "Cataclismio3");
        userService.insert(newUser);

        this.mockmvc.perform(post("/auth/reset/cataclismio3@smarthut.xyz"))
                .andDo(print());

        String resetCode = newUser.getResetCode();

        this.mockmvc.perform(post("/auth/reset/cataclismio3@smarthut.xyz/" + resetCode)
                .content("newPassword")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void shouldReturn400OnResendWithVerifiedUser() throws Exception{
        this.mockmvc.perform(post("/auth/resend/mario@smarthut.xyz"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldSuccessfullyResendEmailOnValidData() throws Exception {
        this.mockmvc.perform(post("/auth/resend/unv@smarthut.xyz"))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void testErrorsHasErrors() {
        AuthController authC = new AuthController();
        BindException error = new BindException(new Object(), "something");
        error.addError(new ObjectError("something", "something"));

        assertThrows(BadRequestException.class, () -> authC.login("blah", "something", error));
        assertThrows(BadRequestException.class, () -> authC.changePassword("blah", "something", "blah", error));
    }
}
