package courage.controllers.rest.user_api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controllers.rest.AbstractRestAPI;
import courage.model.entities.User;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/platforms" })
public class UPlatformApi extends AbstractRestAPI<User.Platform, Integer> implements AvoidControl{
}
