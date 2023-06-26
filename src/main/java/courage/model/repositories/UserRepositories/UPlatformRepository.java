package courage.model.repositories.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import courage.model.entities.User;
import courage.model.repositories.UserRepository.AvoidRepository;

public interface UPlatformRepository extends AvoidRepository<User.Platform, Integer> {
}