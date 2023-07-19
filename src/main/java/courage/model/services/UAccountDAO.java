package courage.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import courage.model.entities.UAccount;
import courage.model.repositories.UAccountRepository;

@Service
public class UAccountDAO {

    @Autowired
    protected PasswordEncoder encode;
    @Autowired
    protected UAccountRepository rep;

    public UAccount save(UAccount entity) {
        entity.setPassword(encode.encode(entity.getPassword()));
        return rep.save(entity);
    }

    public List<UAccount> saveAll(Iterable<UAccount> iterable) {
        for (UAccount e : iterable)
            e.setPassword(encode.encode(e.getPassword()));
        return rep.saveAll(iterable);
    }
}
