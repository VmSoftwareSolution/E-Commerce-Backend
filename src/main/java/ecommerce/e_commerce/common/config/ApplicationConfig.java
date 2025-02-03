package ecommerce.e_commerce.common.config;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce.e_commerce.user.repository.UserRepository;

@Configuration
public class ApplicationConfig {
     
    @Autowired
    private UserRepository userRepository;

    /** 
    * Bean that provides an AuthenticationManager for authentication handling.
    * This manager is responsible for authenticating the credentials of users.
    * 
    * @param config The AuthenticationConfiguration used to get the AuthenticationManager.
    * @return An AuthenticationManager instance.
    * @throws Exception If there is any error in creating the AuthenticationManager.
    */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    /** 
    * Bean that configures the AuthenticationProvider used for authentication.
    * It is configured with a UserDetailsService to load user details and a PasswordEncoder 
    * to verify the password.
    * 
    * @return The AuthenticationProvider instance used for authentication.
    */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /** 
    * Bean that provides the PasswordEncoder used to hash and verify passwords.
    * In this case, BCryptPasswordEncoder is used, which is a secure hashing algorithm.
    * 
    * @return A PasswordEncoder instance used for password encryption.
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 
    * Bean that provides a UserDetailsService for loading user details from the database.
    * It retrieves the user by their email (username) and throws an exception if not found.
    * 
    * @return The UserDetailsService instance for loading user details.
    */
    @Bean
    public UserDetailsService userDetailService() {
        return username -> userRepository.findByEmail(username)
        .orElseThrow(()-> new NoSuchElementException("User not found"));
    }
}
