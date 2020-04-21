package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "ch.usi.inf.sa4.sphinx.service")
public class SphinxApplication {
    @Autowired
    private DummyDataAdder dummyDataAdder;
    @Value("${dummy_data:false}")
    private boolean dummyDataEnabled;


    public static void main(String[] args) {
        SpringApplication.run(SphinxApplication.class, args);
    }


    @PostConstruct
    private void init() {
        //enable dummy_data as an environment variable to add these users
        // (in INTELLIJ SphinxApplication at the left of >
        //edit configurations and set dummy_data=true)
        if (dummyDataEnabled) {
            LoggerFactory.getLogger(SphinxApplication.class).info("dummy_data is enabled");
            dummyDataAdder.deleteUsers();
            dummyDataAdder.user1();
            dummyDataAdder.user2();
            dummyDataAdder.emptyUser();
            dummyDataAdder.unverifiedUser();
            dummyDataAdder.randUser();

        }
    }
}
