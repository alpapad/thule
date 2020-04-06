package uk.co.serin.thule.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.email.controller.EmailController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Autowired
    private BuildProperties buildProperties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).host("piglet").pathMapping("thule-email-service")
                .select().apis(RequestHandlerSelectors.basePackage(EmailController.class.getPackageName()))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .termsOfServiceUrl("https://www.serin-consultancy.co.uk")
                .title("Email Service REST API")
                .version(buildProperties.getVersion())
                .build();
    }
}