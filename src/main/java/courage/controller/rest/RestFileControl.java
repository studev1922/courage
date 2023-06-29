package courage.controller.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.UAccount;

public interface RestFileControl {

   @RestController
   @CrossOrigin("*")
   @RequestMapping({"/api/uploads/account"})
   public class UAccessApi extends AbstractFileAPI {
      UAccessApi() {
         super(UAccount.DIVIDE, UAccount.DIRECTORY);
      }
   }
}
