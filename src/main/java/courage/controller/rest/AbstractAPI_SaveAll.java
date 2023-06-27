package courage.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * POST - PUT methods only receive json body
 * - save without file<br>
 * - save one data json<br>
 * - save all data json<br>
 * 
 * @param E is type of entity
 * @param K is type of entity's key
 * 
 * @see courage.model.entities.User
 * @see courage.controller.rest.AbstractAPI_Read
 */
abstract class AbstractAPI_SaveAll<E, K> extends AbstractAPI_Read<E, K> {

   // @formatter:offs

   @RequestMapping(value = "/all", method = {RequestMethod.POST, RequestMethod.PUT})
   public ResponseEntity<Object> saveAll(Iterable<E> entities) {
      try { // save all data
         return ResponseEntity.ok(rep.saveAll(entities));
      } catch (Exception e) {
         return ResponseEntity.status(400).body(e.getMessage());
      }
   }
}