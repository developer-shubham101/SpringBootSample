package in.newdevpoint.bootcamp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerBearerConfig {

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME = "bearer";

    /**
     * Configures and returns the OpenAPI specification with bearer token authentication.
     *
     * @return an OpenAPI instance including a bearer authentication security scheme and requirement
     */
    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SCHEME_NAME, createBearerScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME));
    }

    /**
     * Creates a SecurityScheme configured for HTTP bearer token authentication.
     *
     * @return a SecurityScheme instance representing the bearer authentication scheme
     */
    private SecurityScheme createBearerScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(SCHEME);
    }
}
