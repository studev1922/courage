package courage.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface Authorization {

    // @formatter:off
    enum R { USER, STAFF, ADMIN, PARTNER } // roles
    enum A { AWAITING, LOCK, PRIVATE, PROTECTED, PUBLIC } // accesses
    enum P { SYSTEM, GOOGLE, FACEBOOK, GITHUB } // platforms

    String[] AUTHENTICATED = {
        "/server/principal"
    };

    String[] PERMIT_ALL = {
        "/static/**", "/client", "index.html",
        "/api/accounts/page"
    };
    
    String[] PERMIT_USER = {
        "/user"
    };
    
    String[] PERMIT_STAFF = {
        "/staff",
    };

    String[] PERMIT_PARTNER = {
        "/partner"
    };

    // include staff, user, partner
    String[] PERMIT_ADMIN = {
        "/api/accounts/**"
    };

    default void authorities(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(PERMIT_ALL).permitAll();

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
            auth.requestMatchers(PERMIT_ADMIN).hasRole(
                R.ADMIN.name()
            );

            auth.requestMatchers(AUTHENTICATED).authenticated();
            auth.anyRequest().permitAll();
        });
    }

    default void authenticate(HttpSecurity http) throws Exception {
        http.exceptionHandling()
            .accessDeniedPage("/server?view=security/_error.htm");

        http.formLogin()
            .loginPage("/server?view=security/_signin.htm")
            .loginProcessingUrl("/security/login") // default [/login]
            .defaultSuccessUrl("/server", false)            
            .failureForwardUrl("/server?view=security/_error.htm");

        http.oauth2Login()
            .failureUrl("/server?view=security/_error.htm")
            .loginPage("/server?view=security/_signin.htm")
            .defaultSuccessUrl("/server", false)
            .authorizationEndpoint()
            .baseUri("/security/login/oauth2");

        http.logout()
            .logoutUrl("/security/logout") // default [/logout]
            .logoutSuccessUrl("/server");
    }
}
