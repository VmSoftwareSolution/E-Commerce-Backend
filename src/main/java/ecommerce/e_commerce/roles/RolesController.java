package ecommerce.e_commerce.roles;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.e_commerce.common.interfaces.roles.RolesControllerInterface;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.dto.PaginationRolesDto;
import ecommerce.e_commerce.roles.dto.UpdateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("roles")
@Tag(name = "roles")
@Validated
public class RolesController implements RolesControllerInterface {

    @Autowired
    private RolesServiceInterface rolesServiceInterface;


    /**
    * Endpoint to create a new role.
    * This method accept's a 'CreateRolesDto' containing the roles details.
    * process the request, and creates a new role in the system
    *
    * @param createRolesDto a {@link CreateRolesDto} object containing the roles details:
    *                       - name (String): The role's name
    *                       - description (String): The roles's description, is optional
    *                       - permission (List<Long>): The roles's permission id (e.g., 1 for create.all, 2 read.all)
    *
    * @return ResponseEntity<?> with status 201 (create) if the roles is successfully registered, and an empty body
    *
    * @throws Exception if there is an error during the user creation process, 
    *         which will be handled by a global exception handler.
    */
    @Operation(
        summary = "Create a new role",
        description = "Create a new role. Requires 'write.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Roles successfully created",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Key (name)=(Guest) already exists.\" }")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not found data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Permission with id = 5 not found.\" }")
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
    @PostMapping
    @PreAuthorize("hasAuthority('write.all')")
    public ResponseEntity<?> createRoles(
        @Valid 
        @RequestBody
        CreateRolesDto createRolesDto
    ) {
        try {
            rolesServiceInterface.createRoles(createRolesDto);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();

        } catch (Exception e) {
            throw e;
        }
    }


    /**
    * Retrieves a list of roles with optional pagination, filtering, and sorting.
    * 
    * This endpoint allows users to fetch roles from the system. The response can be customized 
    * using pagination parameters, sorting options, and filtering by role name.
    * 
    * @param paginationRolesDto A DTO containing optional parameters:
    *        - `flatten` (boolean): If true, returns a simplified response with only `id` and `name`.
    *        - `limit` (int): The maximum number of roles to return (default: 50).
    *        - `offset` (int): The number of roles to skip before collecting results (default: 0).
    *        - `sortOrder` (String): "ASC" or "DESC" sorting order based on role name (default: ASC).
    *        - `name` (String): Filters roles containing this name (case-insensitive).
    * 
    * @return ResponseEntity<?> containing a list of roles with their details. If `flatten` is true, 
    *         only `id` and `name` are included. Otherwise, full role details are returned.
    * @throws Exception If an error occurs while retrieving roles, handled by a global exception handler.
    */
    @Operation(
        summary = "Get all roles",
        description = "Retrieves a list of roles with optional pagination, filtering, and sorting. Requires 'read.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved roles",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = 
                    "[{\"Context\":\"Roles\",\"TotalData\":2,\"Data\":[{\"id\":1,\"name\":\"Admin\",\"description\":\"Administrator role\",\"permission\":[{\"id\":1,\"name\":\"create.all\"}]}]}]"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - insufficient permissions"
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
    public ResponseEntity<?> findRoles(@Valid PaginationRolesDto paginationRolesDto) {
        try {
            List<Map<String,Object>> result
                 = rolesServiceInterface.findRoles(paginationRolesDto);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Updates an existing role with the provided data.
     * <p>
     * This endpoint requires the authority {@code 'write.all'} to access. 
     * The method will update a role based on the provided role ID and 
     * the new values for name, description, and permissions. 
     * If the role is not found, an error will be thrown.
     * </p>
     *
     * @param id The ID of the role to update.
     * @param updateRolesDto The DTO containing the new values for the role fields (name, description, permissions).
     * @return A {@link ResponseEntity} containing the updated role entity with HTTP status 200 (OK).
     * @throws Exception if an unexpected error occurs during processing.
    */
    @Operation(
        summary = "Update a role",
        description = "Updates an existing role with the provided data. Requires 'write.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Role successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = """
                    {
                    "id": 1,
                    "name": "Updated Role",
                    "description": "Updated role description",
                    "permissions": [
                        {
                        "id": 1,
                        "name": "create.all"
                        }
                    ]
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Invalid role name format.\"}")
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - insufficient permissions"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Role with the given ID not found.\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\"}")
            )
        )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write.all')")
    @Override
    public ResponseEntity<?> updateRoles(
        @PathVariable Long id,
        @Valid 
        @RequestBody
        UpdateRolesDto updateRolesDto) {
        try {
            RolesEntity result 
                = rolesServiceInterface.updateRoles(id, updateRolesDto);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Retrieves detailed information about a specific role by its ID.
     * <p>
     * This endpoint fetches a role from the system, including its ID, name, 
     * description, and associated permissions. Each permission is structured 
     * as an object containing its ID and name.
     * </p>
     *
     * @param id The ID of the role to retrieve.
     * @return A {@link ResponseEntity} containing the role details:
     *         <ul>
     *             <li><b>id</b> - The ID of the role.</li>
     *             <li><b>name</b> - The name of the role.</li>
     *             <li><b>description</b> - A brief description of the role.</li>
     *             <li><b>permissions</b> - A list of permissions associated with the role, 
     *                 where each permission contains:
     *                 <ul>
     *                     <li><b>id</b> - The ID of the permission.</li>
     *                     <li><b>name</b> - The name of the permission.</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *         Returns a singleton list containing the role data.
     * @throws Exception if the role with the given ID is not found.
    */
    @Operation(
        summary = "Get role details",
        description = "Retrieves detailed information about a specific role by its ID. Requires 'read.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Role details successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = """
                    {
                        "id": 1,
                        "name": "Admin",
                        "description": "Administrator role",
                        "permissions": [
                            {
                                "id": 1,
                                "name": "create.all"
                            },
                            {
                                "id": 2,
                                "name": "read.all"
                            }
                        ]
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - insufficient permissions"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Role with the given ID not found.\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Unexpected Error\"}")
            )
        )
    })
    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read.all')")
    public ResponseEntity<?> findRolesDetail(@PathVariable Long id) {
        try {
            List<Map<String,Object>> result
                = rolesServiceInterface.findRolesDetail(id);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
        } catch (Exception e) {
            throw e;
        }
    }

    
    
}
