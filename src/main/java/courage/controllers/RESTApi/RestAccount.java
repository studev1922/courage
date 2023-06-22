package courage.controllers.RESTApi;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.Account;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accounts" })
public class RestAccount extends AbstractRESTful<Account, Long> {

}