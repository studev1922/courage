package courage.model.repositories.user;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import courage.model.entities.user.Platform;
import courage.model.repositories.AvoidRepository;

public interface UPlatformRepository extends AvoidRepository<Platform, Integer> {

    @Query(value = "SELECT up_name FROM UPLATFORM LEFT JOIN US_UR ON up_id = upid WHERE u_id =:uid", nativeQuery = true)
    Set<String> findPlatformsByUid(Long uid);
}