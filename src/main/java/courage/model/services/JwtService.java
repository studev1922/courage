package courage.model.services;

import java.text.ParseException;
import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;

public interface JwtService {

    String sign(UserDetails details) throws KeyLengthException, JOSEException;

    UserDetails verify(String token) throws ParseException, JOSEException;
}
