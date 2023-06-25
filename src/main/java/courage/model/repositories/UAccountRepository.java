package courage.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import courage.model.entities.User;

public interface UAccountRepository extends JpaRepository<User.Account, Long> {
}
