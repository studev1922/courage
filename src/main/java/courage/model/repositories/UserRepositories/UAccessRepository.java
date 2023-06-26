package courage.model.repositories.UserRepositories;

import courage.model.entities.User;
import courage.model.repositories.UserRepository.AvoidRepository;

public interface UAccessRepository extends AvoidRepository<User.Access, Integer> {
}