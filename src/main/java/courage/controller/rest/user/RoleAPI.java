package courage.controller.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controller.rest.AbstractAPI_Read;
import courage.model.entities.user.Role;

@RestController
@RequestMapping({ "/api/roles" })
public class RoleAPI extends AbstractAPI_Read<Role, Integer> {
}
