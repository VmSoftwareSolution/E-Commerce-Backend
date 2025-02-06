package ecommerce.e_commerce.common.interfaces.user;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import ecommerce.e_commerce.user.dto.PaginationUserDto;
import jakarta.validation.Valid;

public interface UserControllerInterface {
    ResponseEntity<?> findUser(
        @ParameterObject
        @Valid
        @ModelAttribute
        PaginationUserDto
        paginationUserDto
    );
}
