package ecommerce.e_commerce.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.repository.UserRepository;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;


    /**
    *Creating a new user.
    *This methods accepts a 'CreateUserDto' containing the user details
    * Transform the data to {@Link UserEntity} and calling {@link UserRepository}   
    *
    * @param createUserDto a {@link CreateUserDto} object containing the user details:
    *                         -email (String): The user's email address
    *                         -password (String): The user's password
    *                         -role (Integer): The user's role id (e.g., 1 for user 2 for admin)
    * @Return UserEntity, return the already created user
    */
    @Override
    public UserEntity createUser(CreateUserDto createUser) {
        //Mapping entity
        UserEntity user = new UserEntity();

        user.setEmail(createUser.email);
        user.setPassword(//Encode password using spring security method
            encoder.encode(createUser.password)
        );
        user.setRole(createUser.role);

        return userRepository.save(user);
    }
    
}
