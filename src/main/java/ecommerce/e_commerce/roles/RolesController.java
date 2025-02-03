package ecommerce.e_commerce.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.e_commerce.common.interfaces.roles.RolesControllerInterface;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
    
}
