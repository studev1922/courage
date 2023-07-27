package courage.controller.rest;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.springframework.util.ResourceUtils;
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
import courage.model.services.JwtService;
import courage.model.services.MailService;
import courage.model.services.PropertyService;
import courage.model.util.HtmlTemp;
import courage.model.util.Utils;
import jakarta.mail.MessagingException;
import jakarta.mail.Message.RecipientType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/oauth")
public class OAuthAPI extends RestUAccount {

    final static long AGE = 300; // 300 seconds

    // @formatter:off
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private PropertyService<String> ps;
    @Autowired private HttpServletResponse res;
    @Autowired private MailService mail;
    @Autowired private JwtService jwt;

    @RequestMapping("/get-code")
    public ResponseEntity<?> getCode(@RequestParam String email) {
        try { 
            String code = Utils.generalCode("ES", 9);
            this.ps.put(code, email, System.currentTimeMillis()+AGE*1000);
            this.sendEmail(code, email);
    
            Map<String, Object> map = new HashMap<>();
            map.put("time", AGE);
            map.put("message", Utils.build(
                "An authentication code has been sent to email ",
                email, ", check your email for verification codes"
            ));
            return ResponseEntity.ok().body(Utils.jsonMessage(map));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                Utils.jsonMessage("message", e.getMessage())
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register( UAccount account,
        @RequestParam(required = false) String code,
        @RequestBody(required = false) MultipartFile...files
    ) {
        String message, us, email;
        boolean isEmail;
        try {
            us = account.getUsername();
            email = account.getEmail();
            isEmail = this.ps.get(code).equals(email);

            if(isEmail && ((UAccountRepository) super.rep).exist(us, email)) {
                message = Utils.jsonMessage("message", Utils.build(us, " or ", email, " already exist!"));
            } else if (!isEmail) {
                message = Utils.build("Email ", email , " is not the email that requested the authentication code!");
                message = Utils.jsonMessage("message", message); // append to json
            } else return this.handSaved(code, account, files);
            return ResponseEntity.badRequest().body(message);
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().body(Utils.jsonMessage("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Utils.jsonMessage("message", e.getMessage()));
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
            String bearer = "Bearer " + token;
            res.setHeader("Authorization", bearer);
            return ResponseEntity.ok(Utils.jsonMessage("token", bearer));
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
            "this method not allowed, change the path api/oauth by api/accounts"
        ));
    }

    private ResponseEntity<?> handSaved(String code, UAccount account, MultipartFile[] files) throws Exception{
        account.setUid(-1L); // -1 to save new, uid's identity in database
        ResponseEntity<?> save = super.save(account, files);
        if(save.getStatusCode() == HttpStatus.OK) {
            String token = jwt.sign(Utils.from(account));
            res.setHeader("Authorization", "Bearer " + token);
            this.ps.move(code); // remove code
        }
        return save;
    }
    
    private void sendEmail(String code, String email) throws MessagingException {
        String subject = "Mã xác nhận đăng ký thông tin tài khoản: "+code;
        String text = HtmlTemp.emailCode(code);
        File[] files = null;
        try {
            File[] inpFiles = {
                ResourceUtils.getFile("classpath:properties/code.png")
            };
            files = inpFiles;
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage(), e);
        }
        this.mail.sendMimeMessage(subject, text, files, RecipientType.TO, email);
    }
}
