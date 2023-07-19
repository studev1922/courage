package courage.model.services.impl;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import courage.model.entities.UAccount;
import courage.model.repositories.user.URoleRepository;
import courage.model.services.UAccountDAO;

@Service // @formatter:off
public class AuthService extends UAccountDAO implements UserDetailsService {

	@Autowired private URoleRepository rdao;

	@Override // username || email
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			UAccount e;
			String password;
			Set<String> roles;

			if ((e = rep.findByUnique(username)) == null) {
				throw new UsernameNotFoundException(username + " not found!");
			}
			roles = rdao.findRolesByUid(e.getUid()); //roles
			password = e.getPassword(); // password

			return User.withUsername(username).password(password)
				.roles(roles.toArray(new String[roles.size()]))
				.build();
		} catch (UsernameNotFoundException e) {
			throw e;
		}
	}

}