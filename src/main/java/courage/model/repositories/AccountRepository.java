package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import courage.model.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
