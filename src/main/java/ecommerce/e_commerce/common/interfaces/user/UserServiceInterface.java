package ecommerce.e_commerce.common.interfaces.user;

import java.util.List;
import java.util.Map;

import ecommerce.e_commerce.user.dto.PaginationUserDto;

public interface UserServiceInterface {
    public List<Map<String, Object>> findUser(
        PaginationUserDto paginationUserDto
    );
}
