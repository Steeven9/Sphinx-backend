package ch.usi.inf.sa4.sphinx;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;




@SpringBootApplication
//@PropertySource("classpath:/demo.properties")
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
        if (dummyDataEnabled) {
            dummyDataAdder.dummy1();
        }
    }
}
