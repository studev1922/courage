package courage.controller.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;
import courage.model.services.JwtService;

/**
 * @see RestUAccount.R enumeration of roles
 * @see RestUAccount.A enumeration of accesses
 * @see RestUAccount.P enumeration of platforms
 */
@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accounts" })
public class RestUAccount extends AbstractRESTful<UAccount, Long> {

   // @formatter:off
   enum R { USER, STAFF, ADMIN, PARTNER } // roles
   enum A { AWAITING, LOCK, PRIVATE, PROTECTED, PUBLIC } // accesses
   enum P { SYSTEM, GOOGLE, FACEBOOK } // platforms

   @Autowired private JwtService jwt;
   @Autowired private HttpServletRequest req;
   public RestUAccount() { super(UAccount.DIVIDE, UAccount.DIRECTORY);}

   @PostMapping("/login")
   public ResponseEntity<?> login() { // login by token or (username && password)
      final UAccountRepository dao = ((UAccountRepository) super.rep);
      final String token = req.getHeader("authorization");
      String us = req.getParameter("username");
      String pw = req.getParameter("password");
      if(us == null || us.isEmpty()) us = req.getParameter("unique");

      // return login
      return (token != null && !token.isEmpty())
            ? this.handleToken(dao, token) // by token
            : this.handleLogin(dao, us, pw); // by username and password
   }

   @RequestMapping("/logout")
   public void logout() {
      try {
         req.logout();
      } catch (ServletException e) {
         e.printStackTrace();
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
      account.setUid(null);
      account.setRegTime(null);
      account.setAccess(A.PUBLIC.ordinal());

      if (principal != null) {
         UAccount logged = ((UAccountRepository) rep).findByUsername(principal.getName());
         // if (logged != null && logged.getRoles().contains(R.ADMIN.ordinal()))
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
      if (e == null) return new String[0];
      Set<String> images = e.getImages();
      images.removeAll(Arrays.asList(prevents));
      return images.toArray(new String[0]);
   }

   @Override
   protected void setFiles(UAccount e, Set<String> images) {
      e.setImages(images);
   }

   private ResponseEntity<?> handleToken(UAccountRepository dao, String token) {
      UAccount e; String username; // get UAccount by username

      try {
         token = token.substring(token.lastIndexOf(" "));
         username = jwt.verify(token); // find username by token

         if((e = dao.findByUsername(username)) != null) {
            // TODO req.login(username, e.getPassword()); // servlet login
            return ResponseEntity.ok(e);
         } else 
            return ResponseEntity.status(401).body("account is empty!");
      } catch (Exception ex) {
         ex.printStackTrace();
         return ResponseEntity.status(401).body(ex.getMessage());
      }
   }

   private ResponseEntity<?> handleLogin(UAccountRepository dao, String us, String pw) {
      UAccount e; String username; // get UAccount by username

      try { // sign new token
         // TODO req.login(username, e.getPassword()); // servlet login
         if ((e = ((UAccountRepository) rep).findByUsername(us)) != null) { 
            username = e.getUsername();
            return ResponseEntity.ok(jwt.sign(username)); // create token
         } else {
            return ResponseEntity.status(401).body("account is empty!");
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         return ResponseEntity.status(401).body(ex.getMessage());
      }
   }

   // @formatter:on

}