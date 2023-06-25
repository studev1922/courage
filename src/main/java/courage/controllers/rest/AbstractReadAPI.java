package courage.controllers.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AbstractReadAPI<E, K> {
   @Autowired
   protected JpaRepository<E, K> dao;

   @GetMapping("") // read all
   public ResponseEntity<Object> getData(@RequestParam(required = false) K[] id) {
      try {
         return ResponseEntity.ok(id == null ? dao.findAll()
               : dao.findAllById(java.util.Arrays.asList(id)));
      } catch (Exception e) {
         return ResponseEntity.status(400).body(e.getMessage());
      }
   }

   @GetMapping("/{id}") // reading method to get data
   public ResponseEntity<Object> getData(@PathVariable(required = false) K id) {
      Optional<E> optional = dao.findById(id);
      return optional.isPresent()
            ? ResponseEntity.ok(optional.get())
            : ResponseEntity.noContent().build();
   }
}
