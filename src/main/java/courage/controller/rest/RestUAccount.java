package courage.controller.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import courage.configuration.Authorization;
import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;

/**
 * @see Authorization.R enumeration of roles
 * @see Authorization.A enumeration of accesses
 * @see Authorization.P enumeration of platforms
 */
@RestController
@RequestMapping({ "/api/accounts" })
public class RestUAccount extends AbstractRESTful<UAccount, Long> {

   // @formatter:off
   @Autowired private PasswordEncoder encode;
   @Autowired private HttpServletRequest req;
   public RestUAccount() { super(UAccount.DIVIDE, UAccount.DIRECTORY);}
   
   @Override
   public Example<UAccount> getExample() {
      Principal principal = req.getUserPrincipal();
      UAccount account;

      if (principal != null) {
         account = ((UAccountRepository) rep).findByUnique(principal.getName());
         if (account != null && account.getRoles().contains(Authorization.R.ADMIN.ordinal())) {
            return null; // is logged && is admin, return null to read all
         }
      }

      account = new UAccount(); // by default, only public content is read
      account.setUid(null); account.setRegTime(null);
      account.setAccess(Authorization.A.PUBLIC);
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

   // save one with multipart file
   @Override @RequestMapping(value = { "", "/one" }, method = { RequestMethod.POST, RequestMethod.PUT })
   public ResponseEntity<?> save(UAccount entity, @RequestPart(required = false) MultipartFile... files) {
      this.setPwEncode(Arrays.asList(entity)); // encode password
      return super.save(entity, files);
   }

   // save all without files
   @Override @RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.PUT })
   public ResponseEntity<?> save(Iterable<UAccount> entities) {
      this.setPwEncode(entities);
      return super.save(entities);
   }

   @RequestMapping(value = "/update-pass", method = { RequestMethod.PUT, RequestMethod.PATCH })
   public ResponseEntity<?> updatePassword(String password) {
      try {
         Principal principal = req.getUserPrincipal();
         ((UAccountRepository) super.rep).updatePassword(principal.getName(), encode.encode(password));
         return ResponseEntity.ok().build();
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
      }
   }

   private void setPwEncode(Iterable<UAccount> entities) {
      String pass; // encode password
      for (UAccount e : entities) {
         if ((pass = e.getPassword()) != null)
            e.setPassword(encode.encode(pass));
      }
   }
}