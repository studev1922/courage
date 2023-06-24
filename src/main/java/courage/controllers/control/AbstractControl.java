package courage.controllers.control;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class AbstractControl<E, K> {
   // @formatter:off
	@Autowired protected JpaRepository<E, K> dao;
	@Autowired protected HttpServletRequest req;
	
	@GetMapping("*/{id}") // reading method to get data
	public E getData(@PathVariable(required = false) K id, E or) {
      Optional<E> optional = dao.findById(id);
		return optional.isPresent() ? optional.get() : or;
	}

	// @formatter:on
}
