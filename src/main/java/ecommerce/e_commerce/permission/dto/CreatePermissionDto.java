package ecommerce.e_commerce.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreatePermissionDto {
    
    @Schema(example = "Write.all")
    @NotBlank(message = "Name cannot be null or empty")
    public String name;

    @Schema(example = "create and update in the all modules project")
    public String description;
}
