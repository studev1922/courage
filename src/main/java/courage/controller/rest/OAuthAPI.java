package courage.controller.rest;

import java.io.IOException;
import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.JOSEException;

import courage.model.dto.UserLogin;
import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;
import courage.model.services.CookieService;
import courage.model.services.JwtService;
import courage.model.util.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/oauth")
public class OAuthAPI extends RestUAccount {

    // @formatter:off
    @Autowired private AuthenticationManager authenticationManager;
    // @Autowired private HttpServletRequest req;
    @Autowired private HttpServletResponse res;
    @Autowired private CookieService cookie;
    @Autowired private JwtService jwt;

    @RequestMapping("/confirm-code")
    public ResponseEntity<?> confirmCode(@RequestParam(required = false) String code) {
        try {

            if(code == null) {
                return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Utils.jsonMessage("message", "not allowed code is empty!"));
            } else if(cookie.getCookie(code) != null) {
                cookie.remove(code);
                // TODO: get UAccount from storage
                // UAccount account = new UAccount();
                // return super.save(account);
                System.out.println("confirm code: "+code);
                ResponseEntity.ok().body(Utils.jsonMessage("message", "ACCOUNT SAVED: "+code));
            }
        } catch (DateTimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(Utils.jsonMessage("message", e.getMessage()));
        }
        return ResponseEntity.ok().body(Utils.jsonMessage("message", "TODO: SAVE ACCOUNT"));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(UAccount account, @RequestBody(required = false) MultipartFile...files) {
        account.setUid(-1L); // -1 to save new, uid's identity in database
        String code, us = account.getUsername(), email = account.getEmail();

        if(((UAccountRepository) super.rep).exist(us, email)) {
            return ResponseEntity.badRequest().body(
                Utils.jsonMessage("message",
                    new StringBuilder(us).append(" or ").append(email)
                    .append(" already exist!").toString()
                )
            );
        } else { // TODO: storage account and send the code to email
            code = Utils.generalCode("ES_", 9);
            Map<String, String> attrs = new HashMap<>();
            attrs.put("HttpOnly", "true");
            cookie.setCookie(code, us, 330, attrs); // 5.5 minutes

            System.out.println("get code: "+code);
            return ResponseEntity.ok(
                Utils.jsonMessage("message", "A code sent to the email "+account.getEmail())
            );
        }
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
            return ResponseEntity.ok(Utils.jsonMessage("token", token));
        } catch (JOSEException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Utils.jsonMessage("message", e.getMessage()));
        }
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal UserDetails details) {
        return details!=null
            ? ResponseEntity.ok(details)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override public ResponseEntity<?> save(UAccount entity, MultipartFile... files) {return this.notAllowed();}
    @Override public ResponseEntity<?> save(Iterable<UAccount> entities) {return this.notAllowed();}
    @Override public ResponseEntity<?> updatePassword(String password) {return this.notAllowed();}
    @Override public ResponseEntity<?> delete(Long id) {return this.notAllowed();}
    @Override public ResponseEntity<?> getData(Long[] id) {return this.notAllowed();}
    @Override public ResponseEntity<?> getData(Integer p, Integer s, Direction o, String... f) {return this.notAllowed();}
    @Override public ResponseEntity<?> getData(Long id) {return this.notAllowed();}
    private ResponseEntity<?> notAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(Utils.jsonMessage(
            "message",
            "this method not allowed, change the path \"api/oauth\" by \"api/accounts\""
        ));
    }
}
