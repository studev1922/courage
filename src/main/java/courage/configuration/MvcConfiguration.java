package courage.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // @formatter:off
public class MvcConfiguration implements WebMvcConfigurer {

    protected final static String[] ORIGINS = {
        "http://localhost:8080",
        "http://127.0.0.1:5500",
    };
    protected static final String[] METHODS = {
        "GET", "POST", "PUT", "DELETE",
        "PATCH", "OPTIONS"
    };
    protected static final String[] HEADERS = {
        "Content-Type", "Accept", "Cookie",
        "Authorization", "x-requested-with"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(MvcConfiguration.ORIGINS)
            .allowedMethods(MvcConfiguration.METHODS)
            .allowedHeaders(MvcConfiguration.HEADERS)
            .maxAge(5000);
    }
}
