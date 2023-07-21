package courage.model.repositories.user;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import courage.model.entities.user.Access;
import courage.model.repositories.AvoidRepository;

public interface UAccessRepository extends AvoidRepository<Access, Integer> {

    @Query(value = "SELECT ua_name FROM UACCESS LEFT JOIN US_UA ON ua_id = uaid WHERE u_id =:uid", nativeQuery = true)
    Set<String> findAccessesByUid(Long uid);
}