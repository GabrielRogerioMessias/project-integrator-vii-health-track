package org.projetointegrador.unifio.projectintegratorviibackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Controle de doenças crônicas")
                        .version("V1")
                        .description("API para pacientes portadores de doênças crônicas poderem registrar dados de glicose e pressão arterial")
                        .termsOfService("")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("")));
    }
}
