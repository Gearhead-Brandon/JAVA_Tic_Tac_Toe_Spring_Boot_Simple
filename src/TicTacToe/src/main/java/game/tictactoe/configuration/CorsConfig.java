package game.tictactoe.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-mapping}")
    private String allowedMapping;
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> allowedExposedHeaders;
    private boolean allowCredentials;
    private long maxAge;

    /**
     * Configures CORS mappings for Spring MVC.
     *
     * @param registry The CorsRegistry to configure CORS mappings for.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(allowedMapping)
                .allowedOrigins(allowedOrigins.toArray(String[]::new))
                .allowedMethods(allowedMethods.toArray(String[]::new))
                .allowedHeaders(allowedHeaders.toArray(String[]::new))
                .exposedHeaders(allowedExposedHeaders.toArray(String[]::new))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }
}
