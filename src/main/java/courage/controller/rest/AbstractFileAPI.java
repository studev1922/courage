package courage.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import courage.model.services.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Data;

// @formatter:off
/**
 * @see AbstractFileAPI.OptionFile
 * @see AbstractFileAPI#AbstractFileAPI
 * @see AbstractFileAPI#getFiles : OptionFile || byte[] as file
 */
public abstract class AbstractFileAPI {

   @Data
   @AllArgsConstructor
   static class OptionFile {
      String path;
      String[] fileNames;
   }

   @Autowired protected FileUpload file;
   @Autowired protected HttpServletRequest req;
   @Autowired protected HttpServletResponse res;
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

   /**
    * @see AbstractFileAPI.OptionFile
    * @return OptionFile || byte[] as path file
    */
   @GetMapping({"","/**"}) // get file or folder
   public ResponseEntity<Object> getFiles() {
      String path = this.getPath(); // get path after directory
      int dotPath = path.lastIndexOf("."); // type of file

      // is file has dot type (.[type])
      if(dotPath > -1) {
         String fileName = path.substring(path.lastIndexOf("/")+1);
         return this.toFile(fileName, file.getFile(directory, path));
      }

      return ResponseEntity.ok(new OptionFile(
         file.pathServer(directory, path),
         file.fileNames(true, directory, path)
      ));
   }

   protected String getPath() { // all path variables affter directory
      StringBuffer buffer = req.getRequestURL();
      int start = buffer.lastIndexOf(directory)+directory.length();
      int end = buffer.length();
      return buffer.substring(start, end).toString();
   }
   
   private ResponseEntity<Object> toFile(String fileName, byte[] data) {
      ByteArrayResource resource = new ByteArrayResource(data);
      return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(resource.contentLength())
            .header(
               HttpHeaders.CONTENT_DISPOSITION,
               ContentDisposition
                  .attachment()
                  .filename(fileName)
                  .build()
                  .toString()
            )
            .body(resource);
   }
}
