package ecommerce.e_commerce.common.interfaces.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import jakarta.validation.Valid;

public interface AuthControllerInterface {

  public ResponseEntity<?> registerUser(
    @Valid
    @RequestBody
    CreateUserDto createUser
  );  

  public ResponseEntity<?> loginUser(
    @Valid
    @RequestBody
    CreateUserDto createUser
  );  
} 
