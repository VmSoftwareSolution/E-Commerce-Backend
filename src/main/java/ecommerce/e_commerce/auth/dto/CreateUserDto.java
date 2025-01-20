package ecommerce.e_commerce.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserDto {

    @Schema(example = "victormmosquerag@gmail.com")
    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email must be valid")
    public String email;

    @Schema(example = "mySecretPassword")
    @NotBlank(message = "Password cannot be empty or null")
    public String password;
}
