package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import courage.model.entities.UAccount;

@Repository
public interface UAccountRepository extends JpaRepository<UAccount, Long> {

   /**
    * @see #findByUnique(String)
    */
   @Deprecated
   UAccount findByEmail(String email);

   /**
    * @see #findByUnique(String)
    */
   @Deprecated
   UAccount findByUsername(String username);

   @Query(value = "SELECT * FROM UAccount WHERE :unique IN (username, email)", nativeQuery = true)
   UAccount findByUnique(String unique);

   /**
    * @formatter:off
    * @param unique is username or email
    * @param password encoded by {@link org.springframework.security.crypto.password.PasswordEncoder#encode(CharSequence)}
    * @formatter:on
    */
   @Query(value = "UPDATE UAccount SET password=:password WHERE :unique IN (username, email)", nativeQuery = true)
   void updatePassword(String unique, String password);
}
