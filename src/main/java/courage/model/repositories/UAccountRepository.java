package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import courage.configuration.Authorization;
import courage.model.entities.UAccount;
import jakarta.transaction.Transactional;

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

      @Query("SELECT case when count(uid)>0 then 'true' else 'false' end"
                  + " FROM UAccount WHERE username=:username OR email=:email")
      Boolean exist(String username, String email);

      @Query(value = "SELECT ua.ua_name FROM UACCOUNT a INNER JOIN UACCESS ua"
                  + " ON ua.uaid = a.ua_id WHERE :unique IN (username, email)", nativeQuery = true)
      Authorization.A getAccess(String unique);

      /**
       * @formatter:off
       * @param unique is username or email
       * @param password encoded by {@link org.springframework.security.crypto.password.PasswordEncoder#encode(CharSequence)}
       * @formatter:on
       */
      @Modifying @Transactional
      @Query(value = "UPDATE UAccount SET password=:password WHERE username=:unique OR email=:unique", nativeQuery = true)
      void updatePassword(String unique, String password);
}
