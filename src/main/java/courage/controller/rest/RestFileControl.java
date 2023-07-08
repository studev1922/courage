package courage.controller.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.UAccount;

public interface RestFileControl {
   
   /**
    * Static file on server: http://localhost:8080/uploads/{path...}/{filename}
    * Static file on divide: [local project...]/courage/src/main/webapp/uploads/{path...}/{filename}
    * Read byte[] as file: http://localhost:8080/{[@RequestMapping] path...}/{filename}
    */
   @RestController
   @CrossOrigin("*") // file default api rest controller
   @RequestMapping({ "/api/uploads/default", "/api/uploads/unknown" })
   public class UAccessApi extends AbstractFileAPI {
      UAccessApi() {
         super(UAccount.DIRECTORY);
      }
   }

   @RestController
   @CrossOrigin("*") // account's file api rest controller
   @RequestMapping({ "/api/uploads/account" })
   public class UAccessApi extends AbstractFileAPI {
      UAccessApi() {
         super(UAccount.DIRECTORY);
      }
   }
}
