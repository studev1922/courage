package courage.controllers.api.RestUser;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controllers.api.AbstractRESTful;
import courage.model.entities.User;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/platforms" })
public class UPlatformApi extends AbstractRESTful<User.Platform, Integer> implements AvoidControl{

   @Override
   protected Iterable<Integer> keyParams() {
      return AvoidControl.super.keyParams(super.req);
   }
}
