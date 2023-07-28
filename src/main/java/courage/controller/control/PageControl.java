package courage.controller.control;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import courage.model.util.Utils;

@Controller
@RequestMapping({ "", "/" })
public class PageControl {

   @GetMapping({ "/client" })
   public String index() {
      return "redirect:index.html"; // redirect:http://127.0.0.1:5500/index.html#!/#carousel
   }

   @GetMapping({ "", "/", "/server" })
   public String index(@RequestParam(required = false) String view, Model m) {
      m.addAttribute("view", view != null ? view : "home.htm");
      return "index";
   }

   @ResponseBody
   @GetMapping("/principal") // @formatter:off
   public ResponseEntity<?> getPrincipal(Principal principal) {
      return principal==null
         ? ResponseEntity.status(401)
            .body(Utils.jsonMessage("message", "The principal is null, you're not signed in!"))
         : ResponseEntity.ok(principal);
   }
}
