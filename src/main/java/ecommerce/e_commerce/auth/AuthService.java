package ecommerce.e_commerce.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.repository.UserRepository;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserEntity createUser(CreateUserDto createUser) {
        //Mapping entity
        UserEntity user = new UserEntity();

        user.setEmail(createUser.email);
        user.setPassword(createUser.password);
        user.setRole(createUser.role);

        return userRepository.save(user);
    }
    
}
