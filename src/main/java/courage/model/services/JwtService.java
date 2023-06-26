package courage.model.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {

   @Value("${jwt.secret}")
   private String secret;

   @Value("${jwt.expiration}")
   private Long expiration;

   /**
    * @return new token
    */
   public String sign(String value) {
      long now = System.currentTimeMillis();
      long expDate = now + expiration;

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
      return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

   }

   private final static String hex_code(Object data) {
      MessageDigest messageDigest;
      byte[] bytes;

      try {
         messageDigest = MessageDigest.getInstance("SHA-256");
         bytes = messageDigest.digest(data.toString().getBytes(StandardCharsets.UTF_8));
      } catch (NoSuchAlgorithmException e) {
         System.err.println(e.getMessage());
         bytes = String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8);
      }
      return bytesToHex(bytes);
   }

   private static String bytesToHex(byte[] bytes) {
      StringBuilder hexString = new StringBuilder(2 * bytes.length);
      for (int i = 0; i < bytes.length; i++) {
         String hex = Integer.toHexString(0xff & bytes[i]);
         if (hex.length() == 1) {
            hexString.append('0');
         }
         hexString.append(hex);
      }
      return hexString.toString();
   }
}