package courage.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <h2 style='text-align: center'>POST PUT JSON WITHOUT FILES</h2>
 * 
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
public abstract class AbstractRestAPI<E, K> extends AbstractAPI_Read<E, K> {

	// @formatter:off
   @RequestMapping(value = { "", "/one" }, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> save(E entity) {
		try { // save one data
			return ResponseEntity.ok(rep.save(entity));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

   @RequestMapping(value = "/all", method = {RequestMethod.POST, RequestMethod.PUT})
   public ResponseEntity<?> save(Iterable<E> entities) {
      try { // save all data
         return ResponseEntity.ok(rep.saveAll(entities));
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.status(400).body(e.getMessage());
      }
   }

   @DeleteMapping({ "", "/{id}" }) // Delete method to remove entity
	public ResponseEntity<?> delete(@PathVariable(required = false) K id) {
		if (id != null)
			try { // delete by id
				rep.deleteById(id);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.status(400).body(e.getMessage());
			}
		else return ResponseEntity.noContent().build();
	}
}
