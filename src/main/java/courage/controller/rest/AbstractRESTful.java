package courage.controller.rest;

import java.util.Arrays;
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

/**
 * <h2 style='text-align: center'>POST PUT FORM DATA WITH FILES</h2>
 * POST - PUT methods receive single data by multipart/form-data
 * 
 * @param E is type of entity
 * @param K is type of entity's key
 * 
 * @see courage.model.entities.User
 * @see courage.controller.rest.AbstractAPI_Read
 */
public abstract class AbstractRESTful<E, K> extends AbstractAPI_Read<E, K> {

	// @formatter:off
	@Autowired protected FileUpload file;
	protected final boolean isDevide;
	protected final String directory; // image storage folder
	protected abstract K getKey(E e); // entity's key
	protected abstract String[] filesExist(E e, String...prevents);
	protected abstract void setFiles(E e, Set<String> images);

	/**
	 * @param isDevide folder by entity's id
	 * @param directory is archive folder
	 */
	AbstractRESTful(boolean isDevide, String directory) {
		this.isDevide = isDevide;
		this.directory = directory;
	}

	// save one with multipart file
	@RequestMapping(value = { "", "/one" }, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> save(E entity, @RequestBody(required = false) MultipartFile... files) {
		try {
			entity = this.updateEntity(entity, files);
			return ResponseEntity.ok(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	// save all without multipart file
	@RequestMapping(value = "/all", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> save(Iterable<E> entities) {
		try { 
			for(E e : entities) deletePreviousImages(e); // delete all old images
			Iterable<E> iterable = rep.saveAll(entities); // save all data
			return ResponseEntity.ok(iterable);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(e.getMessage());
		}
   }

	@DeleteMapping({ "", "/{id}" }) // Delete method to remove entity
	public ResponseEntity<?> delete(@PathVariable(required = false) K id) {
		String folder = this.isDevide ? directory + '/' + id : directory;
		if (id != null)
			try {
				Optional<E> o = rep.findById(id);
				if (o.isPresent()) {
					rep.deleteById(id); // delete entity
					if (this.isDevide) file.deleteFile(folder); // delete folder
					else file.deleteFiles(filesExist(o.get()), folder); // delete each file
				}
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(400).body(e.getMessage());
			}
		else
			return ResponseEntity.noContent().build();
	}

	// save file images | e1: previous entity & e2: next entity
	protected E updateEntity(E e, MultipartFile... files) throws Exception {
		// @formatter:off
		boolean hasFiles = files != null;
		String[] prevents = this.deletePreviousImages(e);
		
		// add new all files
		return hasFiles
			? handleFiles(e, prevents, files) // save entity with images
			: this.rep.save(e); // save entity without images
	}

	/**
	 * @param e is entity for update with files
	 * @param prevents all file's names for prevent file deletion
	 * @param files to save all
	 * @return entity updated with all files
	 */
	private E handleFiles(E e, String[] prevents, MultipartFile... files) {
		int i = 0, length = files.length, lengPre = prevents.length;
		String[] images = new String[length + lengPre];
		String folder = String.valueOf(getKey(e));

		// create hash names
		while (i < length) {
			images[i] = FileUpload.hashFileName(
				folder,
				System.currentTimeMillis(),
				files[i++].getOriginalFilename()
			);
		}

		i = 0; --length; // reset index at for prevents're name
		while (length++<lengPre) images[length] = prevents[i++];

		// set new images for insert to database
		this.setFiles(e, new HashSet<>(Arrays.asList(images)));

		i = 0; // reset index at for save file
		length = files.length; // reset length of files
		if (null != (e = this.rep.save(e))) { // save all files when successfully
			folder = this.isDevide ? directory+'/'+getKey(e) : directory; // next
			while (i < length) file.saveFile(images[i], files[i++], folder);
		}

		return e;
	}

	// return all images of new entity
	private String[] deletePreviousImages(E e) {
		String folder;
		K key = getKey(e);
		String[] prevents = filesExist(e);
		Optional<E> o = rep.findById(key);
		String[] fileNames;

		if (o.isPresent()) { // delete previous images not in entity's images
			folder = this.isDevide ? directory + '/' + key : directory; // previous
			fileNames = filesExist(o.get(), prevents);
			if (fileNames.length > 0) file.deleteFiles(fileNames, folder);
		}

		return prevents;
	}

	// @formatter:on
}
