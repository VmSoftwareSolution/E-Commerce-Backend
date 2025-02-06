package ecommerce.e_commerce.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public class UpdateUserDto {
    
    @Schema(example = "victormmosquerag@gmail.com")
    @Email(message = "Email must be valid")
    public String email;

    @Schema(example = "mySecretPassword")
    public String password;

    @Schema(example = "1")
    @Min(1)
    public Long role;
    
}
