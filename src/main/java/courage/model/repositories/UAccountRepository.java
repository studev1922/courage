package courage.model.repositories;

import java.sql.SQLException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import courage.model.entities.UAccount;

public interface UAccountRepository extends JpaRepository<UAccount, Long>, JpaSpecificationExecutor<UAccount> {

   UAccount findByEmail(String email);

   UAccount findByUsername(String username);

   @Query(value = "EXEC pr_login :unique, :password", nativeQuery = true)
   UAccount pr_login(String unique, String password) throws Exception;

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
