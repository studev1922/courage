package courage.controller.control;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "", "/" })
public class PageControl {

   @GetMapping("/server")
   public String index() {
      return "index";
   }

   @ResponseBody
   @GetMapping("/principal")
   public ResponseEntity<Principal> retrievePrincipal(Principal principal) {
      return ResponseEntity.ok(principal);
   }

   @GetMapping
   public String getIndex() {
      // static for client != template on this server
      return "redirect:/index.html"; // redirect to client side
   }
}
