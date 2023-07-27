package courage.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // @formatter:off
public class MvcConfiguration implements WebMvcConfigurer {

    private final static String[] ORIGINS = {
        "http://localhost:8080", // server
        "http://127.0.0.1:5500", // client
    };
    private static final String[] METHODS = {
        "GET", "POST", "PUT", "DELETE", "PATCH"
    };
    private static final String[] HEADERS = {
        "Content-Type", "Authorization"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(MvcConfiguration.ORIGINS)
            .allowedMethods(MvcConfiguration.METHODS)
            .allowedHeaders(MvcConfiguration.HEADERS)
            .maxAge(5000);
    }
}
