package ch.usi.inf.sa4.sphinx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;


/**
 * Configuration class for Swagger for autodocs
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * @return The Docket needed by Springfox to build the docs for the API
     */
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ch.usi.inf.sa4.sphinx.controller"))
                .paths(regex("/((?!docs).*)|(/docs/.+)"))//all paths beside the ones starting with 'docs'
                .build()
                .apiInfo(metaInfo());
    }


    private ApiInfo metaInfo() {
        return new ApiInfo(
                "Sphinx API",
                "Description for Sphinx API", //TODO
                "1.0",
                "Terms", //TODO
                new Contact("Sphinx", "https://localhost:8080",
                        "info@smarthut.xyz"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html"
        );
    }
}

