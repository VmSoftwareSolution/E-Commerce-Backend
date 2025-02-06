package ecommerce.e_commerce.common.interfaces.user;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.dto.UpdateUserDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

public interface UserControllerInterface {
    
    ResponseEntity<?> findUser(
        @ParameterObject
        @Valid
        @ModelAttribute
        PaginationUserDto
        paginationUserDto
    );

    ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @Valid
        @RequestBody UpdateUserDto updateUserDto
    );
}
