package courage.model.repositories.UserRepositories;

import courage.model.entities.User;
import courage.model.repositories.UserRepository.AvoidRepository;

public interface URoleRepository extends AvoidRepository<User.Role, Integer> {
}