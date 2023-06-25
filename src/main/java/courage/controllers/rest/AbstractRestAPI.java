package courage.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * @param <E> is entity
 * @param <K> is the type of entity's key
 **/
public abstract class AbstractRestAPI<E, K> extends AbstractReadAPI<E, K> {

	// @formatter:off

   @RequestMapping(value = { "", "/{any}" }, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Object> saveOne(
      E entity, MultipartFile[] files
   ) {
		try {
			return ResponseEntity.ok(dao.save(entity));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Object> saveAll(@RequestBody Iterable<E> entities) {
		try {
			return ResponseEntity.ok(dao.saveAll(entities));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

   @DeleteMapping({ "", "/{id}" }) // Delete method to remove entity
	public ResponseEntity<Object> delete(@PathVariable(required = false) K id) {
		if (id != null)
			try {
				dao.deleteById(id);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.status(400).body(e.getMessage());
			}
		else
			return ResponseEntity.noContent().build();
	}
}
