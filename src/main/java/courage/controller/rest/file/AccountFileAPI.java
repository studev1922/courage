package courage.controller.rest.file;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controller.rest.AbstractFileAPI;
import courage.model.entities.UAccount;

@RestController
@CrossOrigin("*") // account's file api rest controller
@RequestMapping({ "/api/uploads/account" })
public class AccountFileAPI extends AbstractFileAPI {
   AccountFileAPI() {
      super(UAccount.DIRECTORY);
   }
}
