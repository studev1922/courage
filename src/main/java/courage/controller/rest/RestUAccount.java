package courage.controller.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;
import courage.model.util.Authorization;

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

   @Autowired private PasswordEncoder encode;
   @Autowired private HttpServletRequest req;
   public RestUAccount() { super(UAccount.DIVIDE, UAccount.DIRECTORY);}
   
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
      account.setAccess(Authorization.A.PUBLIC.ordinal());

      if (principal != null) {
         UAccount logged = ((UAccountRepository) rep).findByUsername(principal.getName());
         // if (logged != null && logged.getRoles().contains(Authorization.R.ADMIN.ordinal()))
            return null; // is logged && is admin, return null to read all
      }
      return Example.of(account);
   }

   @Override
   protected Long getKey(UAccount e) { return e.getUid(); }

   @Override
   protected String[] filesExist(UAccount e, String... prevents) {
      if (e == null) return new String[0];
      Set<String> images = e.getImages();
      images.removeAll(Arrays.asList(prevents));
      return images.toArray(new String[0]);
   }

   @Override
   protected void setFiles(UAccount e, Set<String> images) { e.setImages(images); }

   // @formatter:on

   // save one with multipart file
   @RequestMapping(value = { "", "/one" }, method = { RequestMethod.POST, RequestMethod.PUT })
   public ResponseEntity<?> save(UAccount entity, @RequestBody(required = false) MultipartFile... files) {
      this.setPwEncode(Arrays.asList(entity)); // encode password
      return super.save(entity, files);
   }

   // save all without files
   @RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.PUT })
   public ResponseEntity<?> save(Iterable<UAccount> entities) {
      this.setPwEncode(entities);
      return super.save(entities);
   }

   private void setPwEncode(Iterable<UAccount> entities) {
      String pass;
      for (UAccount e : entities) { // encode password
         if ((pass = e.getPassword()) != null)
            e.setPassword(encode.encode(pass));
      }
   }
}