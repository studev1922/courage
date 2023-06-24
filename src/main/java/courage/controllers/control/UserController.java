package courage.controllers.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import courage.model.entities.UAccount;
import courage.model.entities.User;

@Controller
public class UserController extends AbstractControl<UAccount, Long> {

   // @formatter:off
   @Autowired private JpaRepository<User.Access, Integer> adao;

   @GetMapping("/thong-tin-tai-khoan/{id}")
   public String showDetail(@PathVariable(required = false) Long id) {
      UAccount e = super.getData(id, new UAccount());
      req.setAttribute("entity", e);
      req.setAttribute("uaccess", adao.findAll());
      req.setAttribute("view", "/pages/account/detail");

      System.out.println(req.getRequestURL());
      return "index";
   }
}
