package courage.controller.rest.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controller.rest.AbstractAPI_Read;
import courage.model.entities.user.Platform;

@RestController
@RequestMapping({ "/api/platforms" })
public class PlatformAPI extends AbstractAPI_Read<Platform, Integer> {
}
