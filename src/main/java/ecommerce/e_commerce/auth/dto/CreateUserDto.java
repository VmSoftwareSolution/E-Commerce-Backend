package ecommerce.e_commerce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateUserDto {

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email must be valid")
    public String email;

    @NotBlank(message = "Password cannot be empty or null")
    public String password;

    @Min(value = 1, message = "Role must be greater than zero")
    public int role;
}
