package courage.controller.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * READ only data
 * - single data by id <br>
 * - multiple by list id<br>
 * - all data <br>
 * 
 * @param E is type of entity
 * @param K is type of entity's key
 * 
 * @see courage.model.entities.User
 */
public abstract class AbstractAPI_Read<E, K> {

   // @formatter:off
   @Autowired protected JpaRepository<E, K> rep;

   @GetMapping("") // read all or by multiple ids
   public ResponseEntity<Object> getData(@RequestParam(required = false) K[] id) {
      try {
         return ResponseEntity.ok(id == null ? rep.findAll()
               : rep.findAllById(java.util.Arrays.asList(id)));
      } catch (Exception e) {
         return ResponseEntity.status(400).body(e.getMessage());
      }
   }

   @GetMapping("/{id}") // read by single id
   public ResponseEntity<Object> getData(@PathVariable(required = false) K id) {
      Optional<E> optional = rep.findById(id);
      return optional.isPresent()
            ? ResponseEntity.ok(optional.get())
            : ResponseEntity.noContent().build();
   }
}
