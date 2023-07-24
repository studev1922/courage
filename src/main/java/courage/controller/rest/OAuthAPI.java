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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import courage.model.dto.UserLogin;
import courage.model.entities.UAccount;
import courage.model.services.JwtService;
import courage.model.services.UAccountDAO;
import courage.model.util.util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/oauth")
public class OAuthAPI {

    // @formatter:off
    @Autowired private AuthenticationManager authenticationManager;
    // @Autowired private HttpServletRequest req;
    @Autowired private HttpServletResponse res;
    @Autowired private JwtService jwt;
    @Autowired private UAccountDAO dao;

    @RequestMapping("/register")
    public ResponseEntity<?> register(UAccount account) {
        account = dao.register(account);
        return account == null
            ? ResponseEntity.badRequest()
                .body(util.jsonMessage("message", "create user failed!"))
            : ResponseEntity.ok(account);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin account) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            account.getUsername(), account.getPassword()
        );
        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            String token = jwt.sign((UserDetails) authentication.getPrincipal());
            res.setHeader("Authorization", "Bearer " + token);
            return ResponseEntity.ok(util.jsonMessage("token", token));
        } catch (JOSEException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(util.jsonMessage("message", e.getMessage()));
        }
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal UserDetails details) {
        return details!=null
            ? ResponseEntity.ok(details)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
