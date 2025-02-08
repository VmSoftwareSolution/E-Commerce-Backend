package ecommerce.e_commerce.permission;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.e_commerce.common.interfaces.permission.PermissionControllerInterface;
import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.dto.PaginationPermissionDto;
import ecommerce.e_commerce.permission.dto.UpdatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("permission")
@Tag(name = "permission")
@Validated
public class PermissionController implements PermissionControllerInterface{
    
    @Autowired
    private PermissionServiceInterface permissionServiceInterface;


    /** 
    * Endpoint to create a new Permission.
    * This method accept's a 'CreatePermissionDto' containing the permission details.
    * Process the request, and creates a new permission in the system
    *
    * @param createPermissionDto a {@Link CreatePermissionDto} object containing the permission details:
    *                            - name (String): the permissions's name
    *                            - description (String): the permission's description, is optional
    *
    * @return ResponseEntity<?> with status 201 (create) if the permissions is successfully registered, and an empty body
    *
    * @throws Exception if there is an error during the user creation process, 
    *         which will be handled by a global exception handler.  
    */
    @Operation(
        summary = "Create a new Permission",
        description = "Create a new permission. Requires 'write.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Permission successfully created",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Key (name)=(Write.all) already exists.\" }")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\" }")
            )
        )
    })
    @Override
    @PreAuthorize("hasAuthority('write.all')")
    @PostMapping
    public ResponseEntity<?> createPermission(
        @Valid 
        @RequestBody 
        CreatePermissionDto createPermissionDto
    ) {
        try {
            permissionServiceInterface.createPermission(createPermissionDto);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
        } catch (Exception e) {
            throw e;
        }
    }



    /**
    * Endpoint to retrieve a list of permissions.
    * 
    * This method fetches permissions based on optional pagination and filtering criteria 
    * provided via the {@link PaginationPermissionDto}.
    * 
    * The method supports:
    * - Retrieving all permissions if no filters are applied.
    * - Filtering results by permission name.
    * - Sorting results in ascending (default) or descending order.
    * - Paginating results with offset and limit.
    * - Returning a flattened response if `flatten` is set to true.
    * 
    * @param paginationPermissionDto an optional {@link PaginationPermissionDto} containing
    *                                pagination and filtering options.
    * 
    * @return ResponseEntity<?> with status 200 (OK) containing a list of permissions.
    * 
    * @throws Exception if an unexpected error occurs during processing.
    */
    @Operation(
        summary = "Get all Permission",
        description = "Retrieves a list of permission with optional pagination, filtering, and sorting. Requires 'read.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Permissions successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "[{"
                    + "\"Context\": \"Permission\","
                    + "\"TotalData\": 2,"
                    + "\"Data\": ["
                    + "    {\"id\": 1, \"name\": \"read.all\", \"description\": \"read all modules\"},"
                    + "    {\"id\": 2, \"name\": \"write.all\", \"description\": \"write all modules\"}"
                    + "]"
                    + "}]"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - User lacks necessary permissions"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\" }")
            )
        )
    })
    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('read.all')")
    public ResponseEntity<?> findPermission(@Valid PaginationPermissionDto paginationPermissionDto) {
        try {
            List<Map<String,Object>> foundPermission 
                = permissionServiceInterface.findPermission(paginationPermissionDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(foundPermission); 
        } catch (Exception e) {
            throw e;
        }
    }



        /**
    * Endpoint to update an existing permission.
    * 
    * This method allows updating a permission entity based on its ID and the provided 
    * {@link UpdatePermissionDto}. Only the provided fields will be updated.
    * 
    * @param id The ID of the permission to update.
    * @param updatePermissionDto A DTO containing the updated values for the permission.
    * 
    * @return ResponseEntity<?> with status 200 (OK) containing the updated permission.
    * 
    * @throws NoSuchElementException if the permission with the given ID is not found.
    * @throws Exception if an unexpected error occurs during processing.
    */
    @Operation(
        summary = "Update a Permission",
        description = "Updates an existing permission. Requires 'write.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Permission successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{"
                    + "\"id\": 1,"
                    + "\"name\": \"write.all\","
                    + "\"description\": \"Updated permission description\""
                    + "}")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Permission not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Permission with id = 1 not found.\" }")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\" }")
            )
        )
    })
    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write.all')")
    public ResponseEntity<?> updatePermission(Long id, @Valid UpdatePermissionDto updatePermissionDto) {
        try {
            PermissionEntity result 
                = permissionServiceInterface.updatePermission(id, updatePermissionDto);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
        } catch (Exception e) {
            throw e;
        }
    }
    

}
