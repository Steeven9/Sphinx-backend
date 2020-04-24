package ch.usi.inf.sa4.sphinx.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @Test
    public void shouldGet401OnLoginWithWrongUsername() throws Exception {
        this.mockmvc.perform(post("/auth/login/test321")
                    .content("gfgfgfgf")
                    .header("content-type", "application/json"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Disabled
    @Test
    public void shouldGet400OnLoginWithWrongEmail() throws Exception {
        this.mockmvc.perform(post("/auth/login")
                .content("{email:\"test@email.io\"}")
                .header("content-type", "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
