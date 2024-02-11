Adding Swagger to a Spring Boot project involves a few steps. Swagger simplifies API development by automatically generating interactive API documentation from your annotations.

Here's how to add Swagger to your project:

1. **Add Swagger dependencies**: You need to include the Swagger dependencies in your `pom.xml` file if you're using Maven or `build.gradle` if you're using Gradle.

For Maven, add the following dependencies:

```xml
<dependencies>
    <!-- Swagger dependencies -->
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-boot-starter</artifactId>
        <version>3.0.0</version> <!-- Update version to the latest available -->
    </dependency>
</dependencies>
```

For Gradle, add the following dependencies:

```groovy
implementation 'io.springfox:springfox-boot-starter:3.0.0' // Update version to the latest available
```

2. **Enable Swagger in your Spring Boot application**: You need to configure Swagger to be enabled in your Spring Boot application.

Create a configuration class to enable Swagger. Here's an example:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
```

This configuration class enables Swagger and configures it to scan all your API endpoints.

3. **Access Swagger UI**: Once you've added Swagger to your project and configured it, you can access the Swagger UI by navigating to `/swagger-ui/index.html` in your web browser. For example, if your application is running locally on port 8080, you would access Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

That's it! With these steps, you've added Swagger to your Spring Boot project, and you can now use it to document and test your APIs interactively.