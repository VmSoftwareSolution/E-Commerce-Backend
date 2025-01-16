package ecommerce.e_commerce.common.interfaces.permission;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import jakarta.validation.Valid;

public interface PermissionControllerInterface {
     public ResponseEntity<?> createPermission(@Valid @RequestBody CreatePermissionDto createPermissionDto);
}
