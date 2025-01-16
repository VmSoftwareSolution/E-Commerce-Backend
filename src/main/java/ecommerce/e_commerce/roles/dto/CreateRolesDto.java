package ecommerce.e_commerce.roles.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateRolesDto {
    
    @NotBlank(message = "Name cannot be empty or null")
    public String name;

    public String description;

    @Min(value = 1, message = "Permission must be greater than zero")
    public int permission;
}
