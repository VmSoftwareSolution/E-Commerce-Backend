package ecommerce.e_commerce.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.common.interfaces.auth.AuthControllerInterface;
import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@Validated
public class AuthController implements AuthControllerInterface{
    
    @Autowired
    private AuthServiceInterface authServiceInterface;

    /**
    * Endpoint to register a new user.
    * This method accepts a `CreateUserDto` containing the user details, 
    * processes the request, and creates a new user in the system.
    *
    * @param createUser a {@link CreateUserDto} object containing the user details:
    *                   - email (String): The user's email address.
    *                   - password (String): The user's password.
    *                   - role (Integer): The user's role (e.g., 1 for user, 2 for admin).
    * @return ResponseEntity<?> with status 201 (Created) if the user is successfully registered, 
    *         and an empty body.
    * @throws Exception if there is an error during the user creation process, 
    *         which will be handled by a global exception handler.
    */
    @Override
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
        @Valid 
        @RequestBody 
        CreateUserDto createUser
    ){
        try {
            authServiceInterface.createUser(createUser);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
        } catch (Exception e) {
            throw e;
        }
    }

    
}
