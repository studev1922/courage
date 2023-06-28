package courage.model.entities;

import org.springframework.web.multipart.MultipartFile;

import courage.model.entities.User.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public interface FormData {

   MultipartFile[] getFiles();

   void setFiles(MultipartFile[] files);

   // @formatter:off
   @Data @NoArgsConstructor @AllArgsConstructor
   @EqualsAndHashCode(callSuper = true) // form account
   class FAccount extends Account implements FormData {
      private MultipartFile[] files; // account with file
   }
}
