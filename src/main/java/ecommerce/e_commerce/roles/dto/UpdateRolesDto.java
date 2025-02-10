package ecommerce.e_commerce.roles.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class UpdateRolesDto {

    @Schema(example = "Guest")
    public String name;

    @Schema(example = "user with someone permissions")
    public String description;

    @Schema( example = "[1,2]")
    @Size(
        min = 1, 
        message = "At least one permission is required"
    )
    public List<Long> permission;
}
