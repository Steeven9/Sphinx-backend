package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SphinxApplicationTests {

	@Autowired
	private UserController userController;

	@Test
	public void contextLoads() {
		assertThat(userController).isNotNull();
	}

}
