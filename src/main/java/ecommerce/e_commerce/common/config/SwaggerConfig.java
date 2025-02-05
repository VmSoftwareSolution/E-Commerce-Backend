package ecommerce.e_commerce.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition(
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"  
)
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