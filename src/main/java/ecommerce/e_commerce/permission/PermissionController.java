package ecommerce.e_commerce.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.e_commerce.common.interfaces.permission.PermissionControllerInterface;
import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("permission")
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
    @Override
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
    

}
