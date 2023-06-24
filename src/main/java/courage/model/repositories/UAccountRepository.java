package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import courage.model.entities.UAccount;

public interface UAccountRepository extends JpaRepository<UAccount, Long> {
}
