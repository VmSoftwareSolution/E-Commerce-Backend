package ecommerce.e_commerce.roles.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PaginationRolesDto {
    
    @Schema(description = "Indicates whether the response should be flattened", example = "true")
    private boolean flatten;

    @Schema(description = "Maximum number of results to return", example = "10")
    @Max(value = 50, message = "Limit cannot exceed 50")
    private int limit;

    @Schema(description = "Number of records to skip for pagination", example = "0")
    @Min(value = 0, message = "Offset cannot be negative")
    private int offset;

    @Schema(description = "Sorting order: ASC for ascending or DESC for descending", example = "ASC")
    @Pattern(
        regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE, 
        message = "Sort order must be either ASC or DESC"
    )
    private String sortOrder;

    @Schema(description = "Filter users by name", example = "Admin")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
