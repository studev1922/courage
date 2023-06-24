package courage.controllers.rest.user_api;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

public interface AvoidControl {

   default Iterable<Integer> keyParams(HttpServletRequest req) {
      String[] params = req.getParameterValues("id");
      if(params == null) return new ArrayList<Integer>();
      
      Integer[] ids = new Integer[params.length];
      for (int i = 0; i < ids.length; i++) {
         try {
            ids[i] = new Integer(params[i]);
         } catch (Exception e) {
            ids[0] = 0;
         }
      }
      return Arrays.asList(ids);
   }
}
