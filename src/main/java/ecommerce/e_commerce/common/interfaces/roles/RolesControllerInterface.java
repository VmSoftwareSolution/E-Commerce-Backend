package ecommerce.e_commerce.common.interfaces.roles;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import jakarta.validation.Valid;

public interface RolesControllerInterface {
    
    public ResponseEntity<?> createRoles(
        @Valid
        @RequestBody
        CreateRolesDto createRolesDto
    );
}
