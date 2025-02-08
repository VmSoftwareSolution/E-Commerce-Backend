package ecommerce.e_commerce.common.interfaces.permission;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.dto.PaginationPermissionDto;
import jakarta.validation.Valid;

public interface PermissionControllerInterface {
     public ResponseEntity<?> createPermission(
          @Valid 
          @RequestBody 
          CreatePermissionDto createPermissionDto
     );

     public ResponseEntity<?> findPermission(
          @ParameterObject
          @Valid
          @ModelAttribute
          PaginationPermissionDto
          paginationPermissionDto
     );
}
