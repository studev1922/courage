package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import courage.model.entities.User;

public interface UAccountRepository extends JpaRepository<User.Account, Long> {
   User.Account findByUsername(String username) throws Exception;

   @Query(value = "EXEC pr_login :username, :password", nativeQuery = true)
   User.Account pr_login(String username, String password) throws Exception;
}
