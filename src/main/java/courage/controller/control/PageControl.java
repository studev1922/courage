package courage.controller.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "", "/" })
public class PageControl {

   @GetMapping("/server")
   public String index() {
      return "index";
   }

   @GetMapping
   public String getIndex() {
      // static for client != template on this server
      return "redirect:/index.html"; // redirect to client side
   }
}
