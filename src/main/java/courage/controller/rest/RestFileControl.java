package courage.controller.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.UAccount;

public interface RestFileControl {

   /**
    * Static file on server: http://localhost:8080/uploads/account/default.png
    * Static file on divide: [local project...]/courage/src/main/webapp/uploads/account/default.png
    * Read byte[] as file: http://localhost:8080[@RequestMapping]/default.png
    */
   @RestController
   @CrossOrigin("*")
   @RequestMapping({ "/api/uploads/account" })
   public class UAccessApi extends AbstractFileAPI {
      UAccessApi() {
         super(UAccount.DIVIDE, UAccount.DIRECTORY);
      }
   }
}
