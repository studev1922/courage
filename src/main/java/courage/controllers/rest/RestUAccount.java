package courage.controllers.rest;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.model.entities.UAccount;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api/accounts" })
public class RestUAccount extends AbstractRESTful<UAccount, Long> {

   @Override
   protected Iterable<Long> keyParams() {
      String[] params = super.req.getParameterValues("id");
      if(params == null) return new ArrayList<Long>();
      
      Long[] ids = new Long[params.length];
      for (int i = 0; i < ids.length; i++) {
         try {
            ids[i] = new Long(params[i]);
         } catch (Exception e) {
            ids[0] = 0L;
         }
      }
      
      return Arrays.asList(ids);
   }

}