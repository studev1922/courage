package courage.model.services.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import courage.model.services.JwtService;

@Service
public class JwtServiceImpl implements JwtService {

    // studev-courage-1922 as MD5 => 256 bits
    private final byte[] secret = "13513d8e52501d43b64a336e296b6cde".getBytes();
    private final JWSAlgorithm algorithm = JWSAlgorithm.HS256;
    private final Long age = 3600000L; // 1h

    @Override
    public String sign(UserDetails details) throws KeyLengthException, JOSEException {
        // Create a claims set for the JWT payload
        JWTClaimsSet.Builder builder = this.getBuilder();
        this.setUserDetails(builder, details); // set details as all claims
        SignedJWT signed = new SignedJWT(new JWSHeader(algorithm), builder.build());
        signed.sign(new MACSigner(this.secret));
        return signed.serialize(); // Serialize the JWT to a string
    }

    @Override
    public UserDetails verify(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secret);
        boolean valid = signedJWT.verify(verifier); // Verify the signature
        if (!valid)
            throw new JOSEException("invalid to verify!");
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        return this.getUserDetails(claimsSet.getClaims());
    }

    private JWTClaimsSet.Builder getBuilder() {
        return new JWTClaimsSet.Builder().issuer("https://c2id.com")
                .expirationTime(new Date(System.currentTimeMillis() + age));
    }

    private void setUserDetails(JWTClaimsSet.Builder builder, UserDetails details) {
        Function<GrantedAuthority, String> functional = ga -> {
            String authority = ga.getAuthority(); // ROLE_USER => USER
            return authority.substring(authority.indexOf("_") + 1);
        };

        builder.claim("username", details.getUsername()); // set the username
        builder.claim("roles", details.getAuthorities().stream() // set the roles
                .map(functional).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    private UserDetails getUserDetails(Map<String, Object> claims) {
        String username = claims.get("username").toString();
        String[] roles = ((List<String>) claims.get("roles")).toArray(new String[0]);
        return User.withUsername(username)
                .password(String.valueOf(System.currentTimeMillis()))
                .roles(roles).build();
    }
}
