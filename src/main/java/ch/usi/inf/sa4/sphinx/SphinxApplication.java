package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("file:")
public class SphinxApplication {

	@Value("${dummy_enabled:true}")
	private static boolean dummyDataEnabled;

	public static void main(String[] args) {

		SpringApplication.run(SphinxApplication.class, args);
		if (true) {
			DummyDataAdder dda = new DummyDataAdder();
			dda.dummy1();
		}
	}

}
