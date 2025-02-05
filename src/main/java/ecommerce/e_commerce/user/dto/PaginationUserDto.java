package ecommerce.e_commerce.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PaginationUserDto {
    
    @Schema(description = "Indicates whether the response should be flattened", example = "true")
    private boolean flatten;

    @Schema(description = "Maximum number of results to return", example = "10")
    @Max(value = 100, message = "Limit cannot exceed 100")
    private int limit ;

    @Schema(description = "Number of records to skip for pagination", example = "0")
    @Min(value = 0, message = "Offset cannot be negative")
    private int offset;

    @Schema(description = "Sorting order: ASC for ascending or DESC for descending", example = "ASC")
    @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE, 
             message = "Sort order must be either ASC or DESC")
    private String sortOrder ;  

    @Schema(description = "Filter users by email address", example = "email@gmail.com")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @Schema(description = "Filter users by role", example = "Admin")
    @Size(max = 100, message = "Role name cannot exceed 100 characters")
    private String roles;

    
    public boolean isFlatten() {
        return flatten;
    }
    public void setFlatten(boolean flatten) {
        this.flatten = flatten;
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public String getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }

    
}
