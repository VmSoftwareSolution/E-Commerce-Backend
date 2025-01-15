package ecommerce.e_commerce.auth.dto;

import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import ecommerce.e_commerce.user.entity.UserEntity;

@Service
public class AuthService implements AuthServiceInterface {

    @Override
    public UserEntity createUser(CreateUserDto createUser) {
        //Mapping entity
        UserEntity user = new UserEntity();

        user.setEmail(createUser.email);
        user.setPassword(createUser.password);
        user.setRole(createUser.role);

        return user;
    }
    
}
