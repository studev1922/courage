package courage.model.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import courage.model.services.JwtFilter;
import courage.model.services.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

// @formatter:off
@Service
public class JwtFilterImpl implements JwtFilter {

    @Getter
    @Autowired private UserDetailsService userDetailService;
    @Autowired private JwtService jwt;
    private HttpServletRequest req;
    // private HttpServletResponse res;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        this.req = (HttpServletRequest) request;
        // this.res = (HttpServletResponse) response;
        boolean isWithToken = this.handleToken(req.getHeader("Authorization"));
        if(!isWithToken) this.handleLogin(
            req.getParameter("username"),
            req.getParameter("password")
        );
        
        filter.doFilter(request, response); // next to the further work
    }

    private void handleLogin(String username, String password) {
        if(username != null && password != null) {
            // TODO: check login
            System.out.println(username);
            System.out.println(jwt.sign(username));
        }
    }

    private boolean handleToken(String token) {
        if(token == null) return false;
        int indexOf = token.indexOf(" ");
        String unique; // email or username
        
        // get the rest of token
        if(indexOf > -1) try {
            token = token.substring(indexOf);
            unique = jwt.verify(token);
            this.handleAuthentication(userDetailService.loadUserByUsername(unique));
        } catch (UsernameNotFoundException | JwtException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void handleAuthentication(UserDetails details) {
        // TODO: Check logged || login again
        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
            details, null, details.getAuthorities() // Create a new authentication
        );
        // Set the details of the authentication token with the request information
        upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        // Set the authentication token in the security context for the current thread
        SecurityContextHolder.getContext().setAuthentication(upat);
    }

}
