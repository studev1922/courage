package courage.model.repositories.user;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import courage.model.entities.user.Role;
import courage.model.repositories.AvoidRepository;

public interface URoleRepository extends AvoidRepository<Role, Integer> {

    @Query(value = "SELECT role FROM UROLES LEFT JOIN US_UR ON ur_id = urid WHERE u_id =:uid", nativeQuery = true)
    Set<String> findRolesByUid(Long uid);
}