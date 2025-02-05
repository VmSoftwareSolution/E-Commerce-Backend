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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@Tag(name = "auth")
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
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "User successfully created",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Key (email)=(victormmosquerag@gmail.com) already exists.\" }")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\" }")
            )
        )
    })
    @Override
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
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

    /**
    * Endpoint to log in a user.
    * This method accepts a `CreateUserDto` containing the user's email and password, 
    * validates the credentials, and returns a JWT token if the login is successful.
    *
    * @param createUser a {@link CreateUserDto} object containing the user's login details:
    *                   - email (String): The user's email address.
    *                   - password (String): The user's password.
    * @return ResponseEntity<?> with status 200 (OK) and a JWT token in the response body if login is successful.
    *         The token is used for subsequent authentication requests.
    * @throws Exception if there is an error during the login process (e.g., invalid credentials or server error), 
    *         which will be handled by a global exception handler.
    */
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User successfully logged in, token returned",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"token\": \"JWT-TOKEN-HERE\" }")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid login credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Invalid email or password\" }")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\" }")
            )
        )
    })
    @Override
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
        @Valid 
        @RequestBody 
        CreateUserDto createUser
    ){
        try {
            String token =authServiceInterface.loginUser(createUser);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
