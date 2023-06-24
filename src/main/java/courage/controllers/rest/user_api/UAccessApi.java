package courage.controllers.rest.user_api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controllers.rest.AbstractRESTful;
import courage.model.entities.User;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accesses" })
public class UAccessApi extends AbstractRESTful<User.Access, Integer> implements AvoidControl {

   @Override
   protected Iterable<Integer> keyParams() {
      return AvoidControl.super.keyParams(super.req);
   }
}