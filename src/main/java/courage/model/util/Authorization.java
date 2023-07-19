package courage.model.util;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface Authorization {

    // @formatter:off
    enum R { USER, STAFF, ADMIN, PARTNER } // roles
    enum A { AWAITING, LOCK, PRIVATE, PROTECTED, PUBLIC } // accesses
    enum P { SYSTEM, GOOGLE, FACEBOOK } // platforms
    // @formatter:on

    // @formatter:off
    String[] PERMIT_ALL = {
        "/index.html", // this is path client render uses angularjs v1.8
        "/server", // view display on the
        "/api/**",
        "/security/**"
    };
    
    String[] PERMIT_USER = {
        "/user"
    };
    
    String[] PERMIT_STAFF = {
        "/staff"
    };

    String[] PERMIT_PARTNER = {
        "/partner"
    };

    // include staff, user, partner
    String[] PERMIT_ADMIN = {
        "/amdin"
    };

    static HttpSecurity authenticate(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            // USER: any
            auth.requestMatchers(PERMIT_USER).hasAnyRole(
                R.USER.name(), R.STAFF.name(),
                R.PARTNER.name(), R.ADMIN.name()
            );
            // STAFF: staff || admin
            auth.requestMatchers(PERMIT_STAFF).hasAnyRole(
                R.STAFF.name(), R.ADMIN.name()
            );
            // PARTNER: partner || admin
            auth.requestMatchers(PERMIT_PARTNER).hasAnyRole(
                R.PARTNER.name(), R.ADMIN.name()
            );
            // ADMIN: admin
            auth.requestMatchers(PERMIT_ADMIN).hasAnyRole(
                R.ADMIN.name()
            );

            auth.anyRequest().permitAll(); //.authenticated();
        });

        return http;
    }

    static void securityConfig(HttpSecurity http) throws Exception {
        http.formLogin()
            .loginPage("/server?view=security/_signin.htm")
            .loginProcessingUrl("/security/login") // default [/login]
            .defaultSuccessUrl("/server", false);

        http.logout()
            .logoutUrl("/security/logout") // default [/logout]
            .logoutSuccessUrl("/index.html");

        http.oauth2Login()
            .failureUrl("/")
            .loginPage("/server?view=security/_signin.htm")
            .defaultSuccessUrl("/server", false)
            .authorizationEndpoint()
            .baseUri("/security/login/oauth2");
    }
}
