package ecommerce.e_commerce.user;

import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.e_commerce.common.interfaces.user.UserControllerInterface;
import ecommerce.e_commerce.common.interfaces.user.UserServiceInterface;
import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.dto.UpdateUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("users")
@Tag(name = "users")
@Validated
public class UserController implements UserControllerInterface {

    @Autowired
    private UserServiceInterface userServiceInterface;



    /**
    * Retrieves a list of users with optional pagination, filtering, and sorting.
    * <p>
    * This endpoint is secured and requires the authority {@code 'read.all'} to access. 
    * It supports query parameters for pagination, sorting, and filtering based on user roles and email.
    * </p>
    *
    * @param paginationUserDto The pagination and filter criteria for querying users, including:
    *                          <ul>
    *                              <li><b>limit</b> - The maximum number of users to return (default is 50).</li>
    *                              <li><b>offset</b> - The number of users to skip before starting to return results (default is 0).</li>
    *                              <li><b>sortOrder</b> - Sorting order by email, either ASC (default) or DESC.</li>
    *                              <li><b>email</b> - Filter users whose email contains the provided value.</li>
    *                              <li><b>roles</b> - Filter users based on role names.</li>
    *                              <li><b>flatten</b> - If true, returns only user IDs and emails without pagination, sorting, or filtering.</li>
    *                          </ul>
    * @return A {@link ResponseEntity} containing:
    *         <ul>
    *             <li>A list of users with their ID, email, and role information (ID and name).</li>
    *             <li>Metadata including total data count and context information.</li>
    *         </ul>
    *         Returns HTTP status 200 (OK) on success.
    * @throws Exception if an unexpected error occurs during processing.
    */
    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of users with optional pagination, filtering, and sorting. Requires 'read.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Users successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = """
                    {
                      "Context": "User",
                      "TotalData": 2,
                      "Data": [
                        {
                          "id": 1,
                          "email": "john.doe@example.com",
                          "role": {
                            "id": 2,
                            "name": "Admin"
                          }
                        },
                        {
                          "id": 2,
                          "email": "jane.smith@example.com",
                          "role": {
                            "id": 3,
                            "name": "Guest"
                          }
                        }
                      ]
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Invalid value for sortOrder. Only ASC or DESC are allowed.\"}")
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
                schema = @Schema(example = "{\"error\": \"Unexpected Error\"}")
            )
        )
    })
    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('read.all')")
    public ResponseEntity<?> findUser(
        @ParameterObject 
        @Valid 
        @ModelAttribute PaginationUserDto paginationUserDto
    ) {
        try {
            List<Map<String,Object>> foundUser 
                = userServiceInterface.findUser(paginationUserDto);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundUser);
        } catch (Exception e) {
            throw e;
        }
    }



    /**
    * Updates an existing user with the provided data.
    * <p>
     * This endpoint requires the authority {@code 'write.all'} to access. The method will update a user based on
    * the provided user ID and the new values for email, password, and role. If the user is not found, an error will be thrown.
    * </p>
    *
    * @param id The ID of the user to update.
    * @param updateUserDto The DTO containing the new values for the user fields (email, password, role).
    * @return A {@link ResponseEntity} containing the updated user entity with HTTP status 200 (OK).
    * @throws Exception if an unexpected error occurs during processing.
    */
    @Operation(
        summary = "Update a user",
        description = "Updates an existing user with the provided data. Requires 'write.all' authority."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = """
                    {
                      "id": 1,
                      "email": "updated.email@example.com",
                      "role": {
                        "id": 2,
                        "name": "Admin"
                      }
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Invalid email format or role ID.\"}")
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - insufficient permissions"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"User with the given ID not found.\"}")
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
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write.all')")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @Valid
        @RequestBody 
        UpdateUserDto updateUserDto
    ) {
        try {
            UserEntity updateUser 
                = userServiceInterface.updateUser(id, updateUserDto);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateUser);   
        } catch (Exception e) {
            throw e;
        }
    }
    
}
