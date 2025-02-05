package ecommerce.e_commerce.auth.mockData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.user.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthMockData {
    

    private static String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924"; 

    /**
     * Create a DTO for creating a new user.
     * This DTO contains the basic information required to define a user,
     * including its email and password
     * 
     * @return a {@link CreateUserDto} instance pre-filled with default value
    */
    public static CreateUserDto createUserDto(){
        CreateUserDto dto = new CreateUserDto();

        dto.email="JhoeDoe@gmail.com";
        dto.password="mySecretPassword";

        return dto;
    }

    /**
     * Creates a UserEntity object from a CreateUserDto.
     * 
     * @param dto the {@Link CreateUserDto} containing the user details
     * @return a {@Link UserEntity} instance populated with the provided data
    */
    public static UserEntity userEntity(
        CreateUserDto createUserDto
    ){
        UserEntity entity = new UserEntity();
        entity.setEmail(createUserDto.email);
        entity.setPassword(createUserDto.password);

        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setName("Guest");
        rolesEntity.setDescription("user someone permission");
        
        entity.setRole(rolesEntity);

        return entity;
    }

    
    /**
     * Generates a JWT token for an admin user.
     * This token includes default permissions such as "write.all" and "read.all",
     * and it is signed using the HS256 algorithm with a predefined secret key.
     * 
     * The token is valid for 24 minutes from the time of generation.
     *
     * @return a {@link String} representing the signed JWT token with admin permissions
     */
    public static String generateTokenAdmin() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes); 

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + 1000 * 60 * 24);

        Map<String, Object> claims = new HashMap<>();
        
        //Adding permission to Admin
        claims.put("permissions", new String[] {
            "write.all",
            "read.all"
        });

        return Jwts.builder()
                .setClaims(claims)
                .setSubject("victormmosquerag@gmail.com")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
