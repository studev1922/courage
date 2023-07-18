package courage.controller.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
   public ResponseEntity<?> getData(@RequestParam(required = false) K[] id) {
      try {
         return ResponseEntity.ok(id == null ? rep.findAll()
               : rep.findAllById(java.util.Arrays.asList(id)));
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.status(400).body(e.getMessage());
      }
   }

   /**
    * @see AbstractAPI_Read#getData(Integer, Integer, org.springframework.data.domain.Sort.Direction, String...)
    * @return Example to find data
    */
   protected Example<E> getExample() {
      return null; // default find all by pageable
   };

   /**
    * @param p number of page
    * @param s size of data
    * @param o order by "ASC" || "DESC"
    * @param f fields to order by
    * @return ResponseEntity<? extends List<E>>
    */
   @GetMapping("/page")
   public ResponseEntity<?> getData(
      @RequestParam(required = false, defaultValue = "0") Integer p,
      @RequestParam(required = false, defaultValue = "20") Integer s,
      @RequestParam(required = false, defaultValue = "ASC") Sort.Direction o,
      @RequestParam(required = false) String...f
   ) {
      boolean isSort = f != null && f.length > 0;
      PageRequest pageable = isSort 
         ? PageRequest.of(p, s, Sort.by(o, f)) 
         : PageRequest.of(p, s);
      Example<E> example = this.getExample();

      return ResponseEntity.ok(example != null 
         ? rep.findAll(example, pageable) 
         : rep.findAll(pageable)
      );
   }

   @GetMapping("/{id}") // read by single id
   public ResponseEntity<?> getData(@PathVariable(required = false) K id) {
      Optional<E> optional = rep.findById(id);
      return optional.isPresent()
            ? ResponseEntity.ok(optional.get())
            : ResponseEntity.noContent().build();
   }
}
