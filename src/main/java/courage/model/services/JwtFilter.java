package courage.model.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtFilter extends jakarta.servlet.Filter {
    UserDetailsService getUserDetailService();
}
