package courage.model.services;

import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;

/**
 * Server.jwt.algorithm<br>
 * Server.jwt.expiration<br>
 * Server.jwt.secret<br>
 */
public interface JwtService {

   /**
    * <h2>ENCODE</h2>
    * 
    * @see JwtBuilder#setIssuedAt(java.util.Date)
    * @see JwtBuilder#setExpiration(java.util.Date)
    * @see JwtBuilder#signWith(io.jsonwebtoken.SignatureAlgorithm, String)
    *
    * @return jwt buider with (setIssuedAt setExpiration signWith)
    */
   JwtBuilder builder();

   /**
    * <h2>ENCODE</h2>
    * {@link #builder()}
    * 
    * @see JwtBuilder#setSubject(String)
    * @param subject set subject
    * @return {@link JwtBuilder#compact()}
    */
   String sign(String subject);

   /**
    * <h2>ENCODE</h2>
    * {@link #builder()}
    * 
    * @see JwtBuilder#setClaims(Map)
    * @param subject set subject
    * @param claims  set claims
    * @return {@link JwtBuilder#compact()}
    */
   String sign(String subject, Map<String, Object> claims);

   /**
    * <h2>DECODE</h2>
    * 
    * @see JwtParser#setSigningKey(String)
    * @see JwtParser#parseClaimsJws(String)
    * 
    * @param token to decode {@link JwtParser#setSigningKey(String)}
    * @return claims from (setSigningKey, parseClaimsJws)
    * @throws JwtException
    * @throws IllegalArgumentException
    */
   Jws<Claims> claims(String token) throws JwtException, IllegalArgumentException;

   /**
    * <h2>DECODE</h2>
    * {@link #claims(String)}
    * 
    * @param token to decode {@link JwtParser#setSigningKey(String)}
    * @return the body of the Jws<Claims> {@link Jws#getBody()}
    * @throws JwtException
    * @throws IllegalArgumentException
    */
   Claims getBody(String token) throws JwtException, IllegalArgumentException;

   /**
    * <h2>DECODE</h2>
    * {@link #claims(String)}
    * 
    * @param token to decode {@link JwtParser#setSigningKey(String)}
    * @return
    * @throws JwtException
    * @throws IllegalArgumentException
    */
   String verify(String token) throws JwtException, IllegalArgumentException;

   /**
    * <h2>DECODE</h2>
    * {@link #claims(String)}
    * 
    * @param <T>   type of class to convert
    * @param token to decode {@link JwtParser#setSigningKey(String)}
    * @param key   to get <T>
    * @param type  to convert ex Object.class
    * @return an <T> element instance of type
    * @throws JwtException
    * @throws IllegalArgumentException
    */
   <T> T verify(String token, String key, Class<T> type) throws JwtException, IllegalArgumentException;
}