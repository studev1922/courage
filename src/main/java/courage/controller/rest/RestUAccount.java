package courage.controller.rest;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;
import courage.model.services.JwtService;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accounts" })
public class RestUAccount extends AbstractRESTful<UAccount, Long> {

   enum R { // roles
      USER, STAFF, ADMIN, PARTNER
   }

   enum A { // accesses
      AWAITING, LOCK, PRIVATE, PROTECTED, PUBLIC
   }

   enum P { // platforms
      SYSTEM, GOOGLE, FACEBOOK
   }

   // @formatter:off
   @Autowired private JwtService jwt;
   @Autowired private HttpServletRequest req;
   public RestUAccount() { super(UAccount.DIVIDE, UAccount.DIRECTORY);}
   // @formatter:on

   @PostMapping("/login")
   public ResponseEntity<?> login() {
      UAccountRepository dao = ((UAccountRepository) super.rep);
      String token = req.getHeader("authorization");

      return (token != null && !token.isEmpty())
            ? this.handleToken(dao, token) // login by token
            : this.handleLogin(dao); // login by username and password
   }

   @RequestMapping("/logout")
   public void logout() {
      try {
         req.logout();
      } catch (ServletException e) {
         e.printStackTrace();
      }
   }

   // @PreAuthorize
   @RequestMapping(value = "/update-passowrd", method = { RequestMethod.PUT, RequestMethod.PATCH })
   public ResponseEntity<?> updatePassword() {
      // TODO check login session if(isLogin) {...}
      UAccountRepository dao = ((UAccountRepository) super.rep);
      String unique = req.getParameter("unique");
      String password = req.getParameter("password");

      try {
         return (password = dao.update_password(unique, password)) != null
               ? ResponseEntity.ok(password)
               : ResponseEntity.status(500).body("update password failed!");
      } catch (SQLException ex) {
         ex.printStackTrace();
         return ResponseEntity.status(500).body(ex.getMessage());
      }
   }

   @Override
   public Example<UAccount> getExample() {
      /**
       * TODO replace principal by final Authentication
       * SecurityContextHolder.getContext().getAuthentication();
       */
      Principal principal = req.getUserPrincipal();
      UAccount account = new UAccount(); // by default, only public content is read
      account.setAccess(A.PUBLIC.ordinal()); // ua_id > 2 (PROTECTED:3 || PUBLIC:4)

      if (principal != null) {
         UAccount logged = ((UAccountRepository) rep).findByUsername(principal.getName());
         if (logged != null && logged.getRoles().contains(R.ADMIN.ordinal()))
            return null; // is logged && is admin, return null to read all
      }
      return Example.of(account);
   }

   @Override
   protected Long getKey(UAccount e) {
      return e.getUid();
   }

   @Override
   protected String[] filesExist(UAccount e, String... prevents) {
      if (e == null)
         return new String[0];
      Set<String> images = e.getImages();
      images.removeAll(Arrays.asList(prevents));
      return images.toArray(new String[0]);
   }

   @Override
   protected void setFiles(UAccount e, Set<String> images) {
      e.setImages(images);
   }

   private ResponseEntity<?> handleToken(UAccountRepository dao, String token) {
      String username;
      UAccount e;

      try {
         token = token.substring(token.lastIndexOf(" "));
         username = jwt.verify(token); // find username by token
         e = dao.findByUsername(username);
         // TODO req.login(username, e.getPassword()); // servlet login

         return e != null
               ? ResponseEntity.ok(e)
               : ResponseEntity.status(401).body("account is empty!");
      } catch (Exception ex) {
         ex.printStackTrace();
         return ResponseEntity.status(401).body(ex.getMessage());
      }
   }

   private ResponseEntity<?> handleLogin(UAccountRepository dao) {
      String us = req.getParameter("username");
      String pw = req.getParameter("password");
      String username;
      UAccount e;

      try { // sign new token
         e = dao.pr_login(us, pw);

         if (e != null) {
            username = e.getUsername();
            // TODO req.login(username, e.getPassword()); // servlet login
            return ResponseEntity.ok(jwt.sign(username)); // create token
         } else {
            return ResponseEntity.status(401).body("account is empty!");
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         return ResponseEntity.status(401).body(ex.getMessage());
      }
   }

}