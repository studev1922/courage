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
      System.out.println(view);
      return "index.html";
   }

   @GetMapping({ "", "/", "/client" })
   public String staticIndex() {
      // static for client != template on this server
      return "redirect:/index.html"; // redirect to client side
   }

   @ResponseBody
   @GetMapping("/principal")
   public ResponseEntity<Principal> retrievePrincipal(Principal principal) {
      return ResponseEntity.ok(principal);
   }
}
