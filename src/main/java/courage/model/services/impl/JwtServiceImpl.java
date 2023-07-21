package courage.model.services.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import courage.model.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${server.jwt.secret}")
    private String secret = "U1RVREVWLUNPVVJBR0U"; // STUDEV-COURAGE

    @Value("${server.jwt.algorithm}")
    private SignatureAlgorithm algorithm = SignatureAlgorithm.ES512;

    @Value("${server.jwt.expiration}")
    private Long expiration = 300000L; // 5 minutes

    // ENCODE
    // -----------------------------------------------------------------------------
    @Override
    public JwtBuilder builder() {
        long now = System.currentTimeMillis();
        long expDate = now + expiration;

        return Jwts.builder()
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expDate))
                .signWith(algorithm, secret);
    }

    @Override
    public String sign(String subject) {
        return builder().setSubject(subject).compact();
    }

    @Override
    public String sign(String subject, Map<String, Object> claims) {
        return builder()
                .setSubject(subject)
                .setClaims(claims)
                .compact();
    }

    // DECODE
    // -----------------------------------------------------------------------------
    @Override
    public Jws<Claims> claims(String token) throws JwtException, IllegalArgumentException {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
    }

    @Override // decode
    public Claims getBody(String token) throws JwtException, IllegalArgumentException {
        return claims(token).getBody();
    }

    @Override // decode
    public String verify(String token) throws JwtException, IllegalArgumentException {
        return getBody(token).getSubject();
    }

    @Override // decode
    public <T> T verify(String token, String key, Class<T> type) throws JwtException, IllegalArgumentException {
        return getBody(token).get(key, type);
    }
}
