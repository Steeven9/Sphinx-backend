package ch.usi.inf.sa4.sphinx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//@EnableJpaRepositories(basePackages = "com.baeldung.persistence.dao") // already done by @SpringApplication?
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class TestConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String url = env.getProperty("jdbc.url");
        String driver = env.getProperty("jdbc.driver");
        String user = env.getProperty("jdbc.user");
        String password = env.getProperty("jdbc.pass");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setDriverClassName(driver);
        dataSource.setPassword(password);

        return dataSource;
    }

    // configure entityManagerFactory

    // configure transactionManager

    // configure additional Hibernate Properties
}
