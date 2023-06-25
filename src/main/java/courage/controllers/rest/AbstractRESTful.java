package courage.controllers.rest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import courage.model.services.FileUpload;

public abstract class AbstractRESTful<E, K> extends AbstractReadAPI<E, K> {

	// @formatter:off
	@Autowired protected FileUpload file;
	protected final String directory;
	protected abstract K getKey(E e);
	protected abstract String[] filesExist(E e);
	protected abstract void setFiles(E e, Set<String> images);
	AbstractRESTful(String directory) { this.directory = directory; }
	// @formatter:on

	@RequestMapping(value = { "", "/{any}" }, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Object> saveOne(E entity, @RequestBody(required = false) MultipartFile[] files) {
		try {
			Optional<E> o = dao.findById(getKey(entity));
			E e1 = o.isPresent() ? o.get() : null;
			E e2 = dao.save(entity); // save first data
	
			this.updateImage(e1, e2, files);
			return ResponseEntity.ok(e2);
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

	// save file images | e1: old entity & e2: new entity
	protected void updateImage(E e1, E e2, MultipartFile... files) {
		if (e2 == null || files == null) return;
		
		if(e1 != null) file.deleteFiles(filesExist(e1), directory);

		if (this.filesExist(e2).length > 0) { // add new all files
			Set<String> images = new HashSet<>(files.length);
			Long at = System.currentTimeMillis();
			String name = null;

			System.out.println(file.pathLocal(directory));
			System.out.println(file.pathServer(directory));
			for (MultipartFile f : files) {
				name = fileHashName(at, f.getOriginalFilename());
				images.add(file.saveFile(name, f, directory));
				System.out.println(name);
			}

			this.setFiles(e2, images); // set new images
			this.dao.save(e2); // update image to database
		}
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
