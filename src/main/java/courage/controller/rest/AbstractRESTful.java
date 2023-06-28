package courage.controller.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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

import courage.model.entities.FormData;
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
	protected final boolean devide;
	protected final String directory; // image storage folder
	protected abstract K getKey(E e); // entity's key
	protected abstract String[] filesExist(E e, String...prevents);
	protected abstract void setFiles(E e, Set<String> images);

	/**
	 * @param devide folder by entity's id
	 * @param directory is archive folder
	 */
	AbstractRESTful(boolean devide, String directory) {
		this.devide = devide;
		this.directory = directory;
	}
	// @formatter:on

	@RequestMapping(value = { "", "/one" }, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<Object> save(E entity, @RequestBody(required = false) MultipartFile... files) {
		try {
			entity = this.updateEntity(entity, files);
			return ResponseEntity.ok(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.PUT })
	// make sure that entity(form) extends E and implements FormData
	public ResponseEntity<Object> save(Iterable<FormData> entities) {
		Iterator<FormData> iterator = entities.iterator();
		MultipartFile[] files;
		FormData form;
		E e;

		while (iterator.hasNext()) {
			form = (FormData) iterator.next();
			files = form.getFiles();
			e = (E) form;

			// TODO this.updateEntity(e, files); sucess:append to return <> catch printStackTrace
			System.out.println("files.length: " + files.length);
			System.out.println("entity: " + e.toString());
		}

		return ResponseEntity.ok(entities);
	}

	@DeleteMapping({ "", "/{id}" }) // Delete method to remove entity
	public ResponseEntity<Object> delete(@PathVariable(required = false) K id) {
		String folder = this.devide ? directory + '/' + id : directory;
		if (id != null)
			try {
				Optional<E> o = rep.findById(id);
				if (o.isPresent()) {
					rep.deleteById(id);
					if (this.devide) // delete folder
						file.deleteFile(folder);
					else // delete each file
						file.deleteFiles(filesExist(o.get()), folder);
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
		K key = getKey(e);
		boolean hasFiles = files != null;
		String[] prevents = filesExist(e);
		String folder = null;

		Optional<E> o = rep.findById(key);
		if(o.isPresent()) { // delete previous images not in the new images
			folder = this.devide ? directory+'/'+key : directory; // previous
			String[] fileNames = filesExist(o.get(), prevents);
			if(fileNames.length > 0) file.deleteFiles(fileNames, folder);
		}
		
		// add new all files
		return hasFiles
			? handleFiles(e, prevents, files) // save entity with images
			: this.rep.save(e); // save entity without images
		
		// @formatter:on
	}

	/**@formatter:off
	 * @param e is entity for update with files
	 * @param prevents all file's names for prevent file deletion
	 * @param files to save all
	 * @return entity updated with all files
	 */
	private E handleFiles(E e, String[] prevents, MultipartFile... files) {
		int i = 0, length = files.length, lengPre = prevents.length;
		String[] images = new String[length + lengPre];
		String folder = null;

		// create hash names
		while (i < length) {
			images[i] = fileHashName(
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
		// save all files when successfully
		if (null != (e = this.rep.save(e))) {
			folder = this.devide ? directory+'/'+getKey(e) : directory; // next
			while (i < length) file.saveFile(images[i], files[i++], folder);
		}

		return e;
	} // @formatter:on

	// the last parameter must be the file type
	private String fileHashName(Object... names) {
		StringBuilder str = new StringBuilder();
		String type = names[names.length - 1].toString();
		type = type.substring(type.lastIndexOf(".")).trim();
		for (Object name : names)
			str.append(name);
		// hash code array String EX: ["a", 1, 3, x.jpg] => "322623523.jpg"
		return str.hashCode() + type;
	}
}
