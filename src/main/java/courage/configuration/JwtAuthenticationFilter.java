package courage.configuration;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;

import courage.model.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JwtService jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {
        String token = req.getHeader("Authorization");

        res.setHeader ("Access-Control-Allow-Origin", "*");
        res.setHeader ("Access-Control-Allow-Methods", "*");
        res.setHeader ("Access-Control-Allow-Headers", "*");
        res.setHeader ("Access-Control-Max-Age", "3600");
        
        if (token != null && token.startsWith("Bearer ")) try {
            token = token.substring(token.indexOf(" "));
            UserDetails userDetails = jwt.verify(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JOSEException | ParseException | IllegalArgumentException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
        
        if(!req.getMethod().equals("OPTIONS")) filterChain.doFilter(req, res);
    }
}
