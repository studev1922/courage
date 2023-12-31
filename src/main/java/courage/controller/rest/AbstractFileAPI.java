package courage.controller.rest;

import java.util.Base64;

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

import courage.model.services.UploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
   static class OptionFiles {
      String path;
      String[] fileNames;
   }

   @Autowired protected UploadService file;
   @Autowired protected HttpServletRequest req;
   @Autowired protected HttpServletResponse res;
	protected final String directory; // image storage folder
   
   /**
	 * @param directory is archive folder
	 */
	protected AbstractFileAPI(String directory) { this.directory = directory; }

   /**
    * @see AbstractFileAPI.OptionFile
    *
    * @param is == true get files only or false to get folders else get all
    * @return OptionFile || byte[] as path file
    */
   @GetMapping({"","/**"}) // get file or folder
   public ResponseEntity<?> getFiles(
      @RequestParam(required = false) Boolean is,
      @RequestParam(required = false) Boolean base64
   ) {
      String path = this.getPath(); // get path after directory
      int dotPath = path.lastIndexOf("."); // type of file

      if(dotPath > -1) { // is file has dot type (.[type])
         String fileName = path.substring(path.lastIndexOf("/")+1);
         return base64!=null && base64
            ? this.toBase64(fileName, file.getFile(directory, path))
            : this.toFile(fileName, file.getFile(directory, path));
      }

      // default get path api OptionFile
      return ResponseEntity.ok(new OptionFiles(
         file.pathServer(directory, path), // path server to get static file
         file.fileNames(is, directory, path)
      ));
   }

   @PostMapping({"", "/**"})
   public ResponseEntity<?> saveFile(MultipartFile...files) {
      String path = this.getPath(); // get path after directory

      // return path api on server with all files saved
      return ResponseEntity.ok(
         new OptionFiles(
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

      return ResponseEntity.ok().build();
   }

   protected String getPath() { // all path variables affter directory
      StringBuffer buffer = req.getRequestURL();
      int paramAt = buffer.lastIndexOf("?");
      int start = buffer.lastIndexOf(directory)+directory.length();
      int end = paramAt>-1 ? (buffer.length()-paramAt)-1 : buffer.length();
      return buffer.substring(start, end).toString();
   }
   
   // response the file from array bytes
   protected ResponseEntity<?> toFile(String fileName, byte[] data) {
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

   protected ResponseEntity<String> toBase64(String fileName, byte[] data) {
      String base64 = Base64.getEncoder().encodeToString(data);
      return ResponseEntity.ok().body(base64);
   }
}
