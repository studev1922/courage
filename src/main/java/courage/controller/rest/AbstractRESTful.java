package courage.controller.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import courage.model.services.FileUpload;

/**
 * POST - PUT methods receive single data by multipart/form-data
 * POST - PUT methods receive multiple data by json body
 * 
 * @param E is type of entity
 * @param K is type of entity's key
 * 
 * @see courage.model.entities.User
 * @see courage.controller.rest.AbstractAPI_SaveAll
 */
public abstract class AbstractRESTful<E, K> extends AbstractAPI_SaveAll<E, K> {

	// @formatter:off
	@Autowired protected FileUpload file;
	protected final String directory; // image storage folder
	protected abstract K getKey(E e); // entity's key
	protected abstract String[] filesExist(E e);
	protected abstract void setFiles(E e, Set<String> images);
	AbstractRESTful(String directory) { this.directory = directory; }
	// @formatter:on

	@PutMapping({ "", "/one" })
	@PostMapping({ "", "/one" })
	public ResponseEntity<Object> save(E entity, @RequestBody(required = false) MultipartFile[] files) {
		try {
			entity = this.updateEntity(entity, files);
			return ResponseEntity.ok(entity);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@Override
	@PostMapping("/all")
	@PutMapping("/all")
	public ResponseEntity<Object> saveAll(@RequestBody Iterable<E> entities) {
		return super.saveAll(entities);
	}

	@DeleteMapping({ "", "/{id}" }) // Delete method to remove entity
	public ResponseEntity<Object> delete(@PathVariable(required = false) K id) {
		if (id != null)
			try {
				Optional<E> o = rep.findById(id);
				if (o.isPresent()) {
					rep.deleteById(id);
					file.deleteFiles(filesExist(o.get()), directory);
				}
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.status(400).body(e.getMessage());
			}
		else
			return ResponseEntity.noContent().build();
	}

	// save file images | e1: previous entity & e2: next entity
	protected E updateEntity(E e, MultipartFile... files) throws Exception {
		// @formatter:off
		Optional<E> o = rep.findById(getKey(e)); // delete previous images
		if(o.isPresent()) file.deleteFiles(filesExist(o.get()), directory);

		if (this.filesExist(e).length > 0) { // add new all files
			int length = files.length;
			String[] images = new String[length];
			
			// hash file name
			for(int i = 0; i < length; i++)
				images[i] = fileHashName(
					System.currentTimeMillis(),
					files[i].getOriginalFilename()
				);
				
			// set new images
			this.setFiles(e, new HashSet<>(Arrays.asList(images))); 
			
			// save entity with all images when successfully
			if(null != (e = this.rep.save(e))) {
				for(int i = 0; i < length; i++)
					file.saveFile(images[i], files[i], directory);
			}

			return e;
		} else return this.rep.save(e); // save entity without images
		// @formatter:on
	}

	// hash code array String EX: ["a", 1, 3, x.jpg] => "2761525322623523.jpg"
	private String fileHashName(Object... names) {
		StringBuilder str = new StringBuilder();
		String type = names[names.length - 1].toString();
		type = type.substring(type.lastIndexOf(".")).trim();
		for (Object name : names)
			str.append(name);
		return str.hashCode() + type;
	}
}
