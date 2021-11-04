package com.example.webpractice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("人民银行法规库API")
                        .description("人民银行法规库应用")
                        .version("v0.0.1")
                        .license(new License().name("RESTful API").url("https://www.ruanyifeng.com/blog/2014/05/restful_api.html")));
    }
}
