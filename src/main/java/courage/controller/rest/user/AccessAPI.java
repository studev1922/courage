package courage.controller.rest.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controller.rest.AbstractAPI_Read;
import courage.model.entities.user.Access;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accesses" })
public class AccessAPI extends AbstractAPI_Read<Access, Integer> {
}
