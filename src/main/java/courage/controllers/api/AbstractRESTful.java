package courage.controllers.api;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @param <E> is entity
 * @param <K> is the type of entity's key
 **/
public abstract class AbstractRESTful<E, K> {

	// @formatter:off
	@Autowired protected JpaRepository<E, K> dao;
	@Autowired protected HttpServletRequest req;

	protected abstract Iterable<K> keyParams();

	@GetMapping({"", "/{id}"}) // reading method to get data
	public ResponseEntity<Object> getData(@PathVariable(required = false) K id) {
		if (id != null) {// get one by id or get all entities
			Optional<E> optional = dao.findById(id);
			return optional.isPresent()
					? ResponseEntity.ok(optional.get())
					: ResponseEntity.noContent().build();
		} else try {
			Iterable<K> ids = this.keyParams();
			return ResponseEntity.ok(
				ids.iterator().hasNext()
				? dao.findAllById(ids)
				: dao.findAll()
			);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@RequestMapping(value = {"","/{any}"}, method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<Object> saveOne(@RequestBody E entity) {
		try {
			return ResponseEntity.ok(dao.save(entity));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/all", method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<Object> saveAll(@RequestBody Iterable<E> entities) {
		try {
			return ResponseEntity.ok(dao.saveAll(entities));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@DeleteMapping({"", "/{id}"}) // Delete method to remove entity
	public ResponseEntity<Object> delete(@PathVariable(required = false) K id) {
		if (id != null)
			try {
				dao.deleteById(id);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.status(400).body(e.getMessage());
			}
		else return ResponseEntity.noContent().build();
	}

	// @formatter:on
}
