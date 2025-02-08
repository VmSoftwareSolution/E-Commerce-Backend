package ecommerce.e_commerce.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdatePermissionDto {
    
    @Schema(example = "Write.all")
    public String name;

    @Schema(example = "create and update in the all modules project")
    public String description;
}
