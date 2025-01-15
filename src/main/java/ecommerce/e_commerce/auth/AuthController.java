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

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
        @Valid 
        @RequestBody 
        CreateUserDto createUser
    ) {
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
