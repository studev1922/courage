package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import courage.model.entities.UAccount;

@Repository
public interface UAccountRepository extends JpaRepository<UAccount, Long> {

   UAccount findByEmail(String email);

   UAccount findByUsername(String username);
}
