package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("classpath:/demo.properties")
public class SphinxApplication {


	@Value("${dummy_enabled:true}")
	private static boolean dummyDataEnabled;

	public static void main(String[] args) {

		SpringApplication.run(SphinxApplication.class, args);
		if (dummyDataEnabled) {
			System.out.println("HERE");
			DummyDataAdder dda = new DummyDataAdder();
			dda.dummy1();
		}
	}

}
