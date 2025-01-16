package ecommerce.e_commerce.roles.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateRolesDto {
    
    @NotBlank(message = "Name cannot be empty or null")
    public String name;

    public String description;

    @Size(min = 1, message = "At least one permission is required")
    public List<Long> permission;
}
