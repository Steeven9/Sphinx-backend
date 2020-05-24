package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class SphinxApplicationTests {

	@Autowired
	private UserController userController;
	@Value("${spring.datasource.url}")
	String url;
//	@Value("${spring.datasource.platform}")
//	String platform;

	@Test
	public void testPropertiesLoad(){
		assertThat("jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE").isEqualTo(url);

	}

	@Test
	public void contextLoads() {
		assertThat(userController).isNotNull();
	}

}
