package courage.model.repositories;

import java.sql.SQLException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import courage.model.entities.UAccount;

public interface UAccountRepository extends JpaRepository<UAccount, Long> {
   UAccount findByUsername(String username) throws Exception;

   @Query(value = "EXEC pr_login :username, :password", nativeQuery = true)
   UAccount pr_login(String username, String password) throws Exception;

   /**
    * @formatter:off
    *
    * @param unique is username or email
    * @param password to new update
    * @return number of row updated
    * @throws SQLException
    */
   @Query(value = "EXECT pr_update_pass :unique, :password", nativeQuery = true)
   String update_password(String unique, String password) throws SQLException;
}
