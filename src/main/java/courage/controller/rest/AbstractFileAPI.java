package courage.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import courage.model.services.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Data;

// @formatter:off
/**
 * @see AbstractFileAPI.OptionFile
 * @see AbstractFileAPI#AbstractFileAPI(boolean, String)
 * @see AbstractFileAPI#getFiles(Boolean) : OptionFile || byte[] as file
 * @see AbstractFileAPI#saveFile(MultipartFile...)
 * @see AbstractFileAPI#deleteFile(String...)
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
   public ResponseEntity<Object> getFiles(@RequestParam(required = false) Boolean is) {
      String path = this.getPath(); // get path after directory
      int dotPath = path.lastIndexOf("."); // type of file

      if(dotPath > -1 || (is!=null && is)) { // is file has dot type (.[type])
         String fileName = path.substring(path.lastIndexOf("/")+1);
         return this.toFile(fileName, file.getFile(directory, path));
      }

      // default get path api OptionFile
      return ResponseEntity.ok(new OptionFile(
         file.pathServer(directory, path), // path server to get static file
         file.fileNames(true, directory, path)
      ));
   }

   @PostMapping({"", "/**"})
   public ResponseEntity<Object> saveFile(MultipartFile...files) {
      String path = this.getPath(); // get path after directory

      // return path api on server with all files saved
      return ResponseEntity.ok(
         new OptionFile(
            file.pathServer(directory, path),
            file.saveFiles(files, true, directory, path).toArray(new String[0])
         )
      );
   }

   @DeleteMapping({"", "/**"})
   public ResponseEntity<Void> deleteFile (
      @RequestParam(required = false, name = "files") String...fileNames
   ) {
      String path = this.getPath();
      
      if(fileNames!=null)
         file.deleteFiles(fileNames, directory, path); // delete file name
      else try {
         file.deleteFile(directory, path); // delete folder
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.internalServerError().build();
      }

      return ResponseEntity.ok(null);
   }

   protected String getPath() { // all path variables affter directory
      StringBuffer buffer = req.getRequestURL();
      int start = buffer.lastIndexOf(directory)+directory.length();
      int end = buffer.length();
      return buffer.substring(start, end).toString();
   }
   
   // response the file from array bytes
   protected ResponseEntity<Object> toFile(String fileName, byte[] data) {
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
