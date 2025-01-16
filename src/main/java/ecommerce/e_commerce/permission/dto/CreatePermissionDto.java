package ecommerce.e_commerce.permission.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePermissionDto {
    
    @NotBlank(message = "Name cannot be null or empty")
    public String name;

    public String description;
}
