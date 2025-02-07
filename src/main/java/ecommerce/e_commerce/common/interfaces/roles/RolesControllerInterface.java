package ecommerce.e_commerce.common.interfaces.roles;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.dto.PaginationRolesDto;
import jakarta.validation.Valid;

public interface RolesControllerInterface {
    
    public ResponseEntity<?> createRoles(
        @Valid
        @RequestBody
        CreateRolesDto createRolesDto
    );

    public ResponseEntity<?> findRoles(
        @ParameterObject
        @Valid
        @ModelAttribute
        PaginationRolesDto
        paginationRolesDto
    );
}
