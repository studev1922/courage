package courage.controller.rest;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import courage.model.services.FileUpload;

// @formatter:off
public abstract class AbstractFileAPI {
   
	@Autowired protected FileUpload file;
	protected final boolean devide;
	protected final String directory; // image storage folder

   /**
	 * @param devide folder by entity's id
	 * @param directory is archive folder
	 */
	AbstractFileAPI(boolean devide, String directory) {
		this.devide = devide;
		this.directory = directory;
	}
   
   @GetMapping({"","/{folder}"})
   public ResponseEntity<Object> getFiles( // get list file in folder
      @PathVariable(required = false) String folder, // folder in directory
      @RequestParam(required = false) Boolean isDir // get file or folder
   ) {
      folder = folder==null ? directory : new StringBuilder(directory)
            .append('/').append(folder).toString();

      System.out.println(folder);
      return ResponseEntity.ok(
         isDir!=null 
         ? file.fileNames(isDir, folder) 
         : file.pathServer(folder)
      );
   }

}
