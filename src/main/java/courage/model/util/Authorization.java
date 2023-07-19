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
        "/api/**"
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
    String[] PERMIT_ADMIN = util.merger(
        PERMIT_USER, PERMIT_STAFF, PERMIT_PARTNER,  
        new String[] { // path for admin ...
            
        }
    );

    static HttpSecurity authenticate(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(PERMIT_USER).hasAnyRole(R.USER.name());
            auth.requestMatchers(PERMIT_STAFF).hasAnyRole(R.STAFF.name());
            auth.requestMatchers(PERMIT_PARTNER).hasAnyRole(R.PARTNER.name());
            auth.requestMatchers(PERMIT_ADMIN).hasAnyRole(R.ADMIN.name());

            auth.requestMatchers(PERMIT_ALL).permitAll();
            auth.anyRequest().authenticated();
        });

        return http;
    }

    static void securityConfig(HttpSecurity http) throws Exception {
        http.formLogin()
            .loginProcessingUrl("/security/login") // default [/login]
            .defaultSuccessUrl("/", false);

        http.logout()
            .logoutUrl("/security/logout") // default [/logout]
            .logoutSuccessUrl("/");

        http.oauth2Login()
            .failureUrl("/")
            .defaultSuccessUrl("/", false)
            .authorizationEndpoint()
            .baseUri("/security/login/oauth2");
    }
}
