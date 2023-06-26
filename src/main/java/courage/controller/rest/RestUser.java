package courage.controller.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.User;

public interface RestUser {

   @RestController
   @CrossOrigin("*")
   @RequestMapping({ "/api/accesses" })
   public class UAccessApi extends AbstractAPI_Read<User.Access, Integer> {
   }

   @RestController
   @CrossOrigin("*")
   @RequestMapping({ "/api/platforms" })
   public class UPlatformApi extends AbstractAPI_Read<User.Platform, Integer> {
   }

   @RestController
   @CrossOrigin("*")
   @RequestMapping({ "/api/roles" })
   public class URoleApi extends AbstractAPI_Read<User.Role, Integer> {
   }
}
