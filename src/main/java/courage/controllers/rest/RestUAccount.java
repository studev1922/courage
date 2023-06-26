package courage.controllers.rest;

import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.User.Account;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accounts" })
public class RestUAccount extends AbstractRESTful<Account, Long> {

   public RestUAccount() {
      super("account");
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