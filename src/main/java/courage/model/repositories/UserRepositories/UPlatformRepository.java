package courage.model.repositories.UserRepositories;

import courage.model.entities.User;
import courage.model.repositories.UserRepository.AvoidRepository;

public interface UPlatformRepository extends AvoidRepository<User.Platform, Integer> {
}