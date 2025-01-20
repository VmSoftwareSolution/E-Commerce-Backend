package ecommerce.e_commerce.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.repository.UserRepository;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesServiceInterface rolesServiceInterface;

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
        
        //Find roles by name = "guest", if not exist return a error
        Optional<RolesEntity> foundRoles = rolesServiceInterface.findRolesByNameOrFail("Guest");
        user.setRole(foundRoles.get());

        return userRepository.save(user);
    }
    
}
