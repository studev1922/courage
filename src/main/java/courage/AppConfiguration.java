package courage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import courage.model.authHandle.Authorization;
import courage.model.authHandle.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppConfiguration implements Authorization {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // @formatter:off
    SecurityFilterChain filterChain (
        HttpSecurity http,
        JwtAuthenticationFilter filter
    ) throws Exception {
        // TODO: DefaultHandlerExceptionResolver
        http.csrf().disable().cors().disable();
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        this.authorities(http);
        this.authenticate(http);
        return http.build();
    } // @formatter:on

}