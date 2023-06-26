package courage.controller.rest;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.User;
import courage.model.entities.User.Account;
import courage.model.repositories.UAccountRepository;
import courage.model.services.JwtService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accounts" })
public class RestUAccount extends AbstractRESTful<Account, Long> {

   @Autowired
   private JwtService jwt;

   public RestUAccount() {
      super("account");
   }

   @PostMapping("/login")
   public ResponseEntity<Object> login(HttpServletRequest req) {
      UAccountRepository dao = ((UAccountRepository) super.rep);
      String bearer = req.getHeader("authorization");
      String us = req.getParameter("username");
      String pw = req.getParameter("password");
      String token = null;
      User.Account e;

      if (bearer != null && !bearer.isEmpty())
         try {
            token = bearer.substring(bearer.lastIndexOf(" "));
            String username = jwt.verify(token);
            e = dao.findByUsername(username);
            return e != null
                  ? ResponseEntity.ok(e)
                  : ResponseEntity.status(401).body("account is empty!");
         } catch (Exception ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
         }
      else
         try { // sign new token
            e = dao.pr_login(us, pw);
            if (e != null) {
               String username = e.getUsername();
               return ResponseEntity.ok(jwt.sign(username));
            } else {
               return ResponseEntity.status(401).body("account is empty!");
            }
         } catch (Exception ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
         }
   }

   @Override
   protected Long getKey(Account e) {
      return e.getUid();
   }

   @Override
   protected String[] filesExist(Account e) {
      return e == null ? new String[0] : e.getImages().toArray(new String[1]);
   }

   @Override
   protected void setFiles(Account e, Set<String> images) {
      e.setImages(images);
   }

}