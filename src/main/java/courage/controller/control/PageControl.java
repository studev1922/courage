package courage.controller.control;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "", "/" })
public class PageControl {

   @GetMapping("/server")
   public String index(@RequestParam(required = false) String view, Model m) {
      m.addAttribute("view", view != null ? view : "home.htm");
      return "index.html";
   }

   @ResponseBody
   @GetMapping("/principal")
   public ResponseEntity<?> getPrincipal(Principal principal) {
      return principal==null
         ? ResponseEntity.status(401)
            .body("{\"message\":\"the principal is null, you're not signed in!\"}")
         : ResponseEntity.ok(principal);
   }

   @GetMapping({ "", "/", "/client" })
   public String staticIndex() {
      // static for client != template on this server
      return "redirect:/index.html"; // redirect to client side
   }
}
