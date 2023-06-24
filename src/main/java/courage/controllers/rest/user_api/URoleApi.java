package courage.controllers.rest.user_api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controllers.rest.AbstractRESTful;
import courage.model.entities.User;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/roles" })
public class URoleApi extends AbstractRESTful<User.Role, Integer> implements AvoidControl {

   @Override
   protected Iterable<Integer> keyParams() {
      return AvoidControl.super.keyParams(super.req);
   }
}
