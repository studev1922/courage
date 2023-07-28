package courage.model.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import courage.configuration.Authorization.*;
import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;
import courage.model.repositories.user.*;
import courage.model.services.UAccountDAO;

@Service
public class UAccountDAOImpl implements UAccountDAO {

    @Autowired
    private PasswordEncoder encode;
    @Autowired // account repository
    private UAccountRepository rep;
    @Autowired // role repository
    private URoleRepository rrep;
    @Autowired // access repository
    private UAccessRepository arep;
    @Autowired // platform repository
    private UPlatformRepository prep;

    @Override
    public UAccount save(UAccount entity) {
        entity.setPassword(encode.encode(entity.getPassword()));
        return rep.save(entity);
    }

    @Override
    public List<UAccount> saveAll(Iterable<UAccount> iterable) {
        for (UAccount e : iterable)
            e.setPassword(encode.encode(e.getPassword()));
        return rep.saveAll(iterable);
    }

    @Override
    public UAccount findByUnique(String unique) {
        return this.rep.findByUnique(unique);
    }

    @Override
    public Set<String> findRolesByUid(Long uid) {
        return this.rrep.findRolesByUid(uid);
    }

    @Override
    public Set<String> findAccessesByUid(Long uid) {
        return this.arep.findAccessesByUid(uid);
    }

    @Override
    public Set<String> findPlatformsByUid(Long uid) {
        return this.prep.findPlatformsByUid(uid);
    }

    @Override
    public UAccount register(UAccount account) {
        return this.pass(account) ? this.save(account) : null;
    }

    @Override
    public void updatePassword(Long uid, String password) {
        rep.updatePassword(password, encode.encode(password));
    }

    private boolean pass(UAccount account) {
        boolean ispn = account.getRoles().contains(R.PARTNER.ordinal());
        account.setAccess(A.AWAITING);
        account.setRoles(ispn ? R.PARTNER : R.USER);

        if (account.getPlatforms().size() == 0)
            account.setPlatforms(P.SYSTEM);
        if (account.getPassword() == null || account.getUsername() == null)
            return false;
        return true;
    }
}
