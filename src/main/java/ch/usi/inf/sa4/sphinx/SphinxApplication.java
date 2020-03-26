package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class SphinxApplication {

	public static void main(String[] args) {

		SpringApplication.run(SphinxApplication.class, args);
		DummyDataAdder dda =  new DummyDataAdder();
		dda.dummy1();
	}

}
