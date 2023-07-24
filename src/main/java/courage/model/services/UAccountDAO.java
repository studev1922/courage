package courage.model.services;

import java.util.Set;

import courage.model.entities.UAccount;

public interface UAccountDAO extends SuperDAO<UAccount, Long> {

    public UAccount findByUnique(String unique);

    public Set<String> findRolesByUid(Long uid);

    public Set<String> findAccessesByUid(Long uid);

    public Set<String> findPlatformsByUid(Long uid);

    public UAccount register(UAccount account);
}
