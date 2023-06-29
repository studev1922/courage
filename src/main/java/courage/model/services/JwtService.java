package courage.model.services;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {

   @Value("${jwt.secret}")
   private String secret = "U1RVREVWLUNPVVJBR0U="; // STUDEV-COURAGE

   @Value("${jwt.expiration}")
   private Long expiration = 120000L; // 2 minutes

   @Value("${jwt.tobase64}")
   private Boolean toBase64 = false;

   /**
    * @return new token
    */
   public String sign(String value) {
      long now = System.currentTimeMillis();
      long expDate = now + expiration;
      // endcode value to base64
      if (toBase64)
         value = Base64.getEncoder().encodeToString(value.getBytes());

      return Jwts.builder()
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(expDate))
            .signWith(SignatureAlgorithm.HS256, secret)
            .setSubject(value)
            .compact();
   }

   /**
    * @param token to parse value
    * @return value of input param at sign method
    */
   public String verify(String token) throws JwtException, IllegalArgumentException {
      String subject = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
      return toBase64 ? new String(Base64.getDecoder().decode(subject)) : subject;

   }
}