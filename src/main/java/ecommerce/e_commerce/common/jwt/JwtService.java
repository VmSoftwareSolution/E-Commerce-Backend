package ecommerce.e_commerce.common.jwt;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${app.secret-key}")
    private String SECRET_KEY;
    
    /**
    * Generates a JWT token for the given user.
    * 
    * @param user the user for whom the token will be generated
    * @return a JWT token as a String
    */
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    /**
    * Converts the secret key into a Key object for signing JWT tokens.
    * 
    * @return the Key object derived from the secret key
    */
    private Key getKey() {
       byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
    * Generates a JWT token for the given user with optional extra claims (role or permissions).
    * 
    * @param extraClaims additional claims to include in the token
    * @param user the user for whom the token will be generated
    * @return a JWT token as a String
    */
    private String getToken(Map<String,Object> extraClaims, UserDetails user) {

        // Add permissions to extra claims
        extraClaims.put("permissions", user.getAuthorities().stream()
        .map(authority -> authority.getAuthority())
        .toList());

        // Build and sign the token
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))// Valid for 24 minutes
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
    * Extracts the username (subject) from a JWT token.
    * 
    * @param token the JWT token
    * @return the username from the token
    */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    /**
    * Retrieves all claims from a JWT token.
    * 
    * @param token the JWT token
    * @return the claims contained in the token
    */
    public Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
    * Retrieves a specific claim from a JWT token.
    * 
    * @param <T> the type of the claim to retrieve
    * @param token the JWT token
    * @param claimsResolver a function to extract the desired claim
    * @return the value of the specified claim
    */
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
    * Retrieves the expiration date of a JWT token.
    * 
    * @param token the JWT token
    * @return the expiration date of the token
    */
    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    /**
    * Checks if a JWT token has expired.
    * 
    * @param token the JWT token
    * @return true if the token has expired, false otherwise
    */
    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
