package courage.model.services;

import io.jsonwebtoken.JwtException;

public interface JwtService {

   /**
    * @return new token
    */
   public String sign(String value);

   /**
    * @param token to parse value
    * @return value of input param at sign method
    */
   public String verify(String token) throws JwtException, IllegalArgumentException;
}