package courage.controller.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import courage.model.services.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/oauth")
public class OAuthAPI {

    // @formatter:off
    @Autowired private AuthenticationManager authenticationManager;
    // @Autowired private HttpServletRequest req;
    @Autowired private HttpServletResponse res;
    @Autowired private JwtService jwt;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestParam String username,
        @RequestParam String password
    ) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            String token = jwt.sign((UserDetails) authentication.getPrincipal());
            res.setHeader("Authorization", "Bearer "+token);
            return ResponseEntity.ok(authentication.getPrincipal());
        } catch (JOSEException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("\"message\":\""+e.getMessage()+"\"");
        }
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal UserDetails details) {
        return details!=null
            ? ResponseEntity.ok(details)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
