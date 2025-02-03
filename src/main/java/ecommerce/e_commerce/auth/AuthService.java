package ecommerce.e_commerce.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.common.jwt.JwtService;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


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
    
    /**
    * Logs in a user by authenticating their email and password.
    * This method validates the provided credentials using Spring Security's AuthenticationManager, 
    * and if the credentials are valid, it generates and returns a JWT token for authentication.
    *
    * @param createUser a {@link CreateUserDto} object containing the user's login details:
    *                   - email (String): The user's email address.
    *                   - password (String): The user's password.
    * @return String containing the JWT token, which can be used for authenticating future requests.
    * @throws Exception if the credentials are invalid or any error occurs during authentication, 
    *         which will be handled by a global exception handler.
    */
    public String loginUser(CreateUserDto createUser) {
        //Authenticate the user's email and password using Spring Security's AuthenticationManager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(createUser.email, createUser.password));
        //Retrieve the user details from the repository using the email address
        UserDetails user=userRepository.findByEmail(createUser.email).orElseThrow();

        //Generate token and return 
        return jwtService.getToken(user);
    }
}
