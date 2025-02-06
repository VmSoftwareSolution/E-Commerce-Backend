package ecommerce.e_commerce.common.interfaces.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.dto.UpdateUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;

public interface UserServiceInterface {
    
    public List<Map<String, Object>> findUser(
        PaginationUserDto paginationUserDto
    );

    public UserEntity updateUser(
        Long id,
        UpdateUserDto updateUserDto
    );

    //Base methods
    public Optional<UserEntity> findById(Long id);
    public UserEntity findByIdOrFail(Long id);
}
