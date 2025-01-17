package ecommerce.e_commerce.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
    
    /**
     * Configures the OpenAPI documentation for the application.
     * This method sets up the general information about the API,
     * such as the title and version, to be displayed in the Swagger UI.
     *
     * @return an {@link OpenAPI} instance containing the API metadata
     */
    @Bean
    public OpenAPI api(){
        return new OpenAPI().info(new Info().title("ECOMMERCE").version("V1"));
    }
}