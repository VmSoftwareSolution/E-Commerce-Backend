package ecommerce.e_commerce.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Value("${app.api-prefix}")
    private String apiPrefix;
    /**
    * Configures the web application's security, managing access to endpoints
    * and Cross-Site Request Forgery (CSRF) protection settings. The configuration
    * allows unauthenticated access to the registration endpoint, while all
    * other requests require authentication.
    * 
    * @param http an instance of HttpSecurity used to customize web security.
    * @return a SecurityFilterChain that defines the security rules for the application.
    * @throws Exception if an error occurs while configuring security.
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Allow access without authorization
        return http
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers(apiPrefix+"/permission").permitAll()//FIX: Delete this, when using JWT
                .requestMatchers(apiPrefix+"/roles").permitAll()//FIX: Delete this, whe using JWT
                .requestMatchers("/swagger-ui/**", apiPrefix+"/v3/api-docs/**").permitAll()//Without authorization swagger
                .requestMatchers(HttpMethod.POST, apiPrefix+"/auth/register").permitAll()//Without authorization register
                .requestMatchers(HttpMethod.GET,apiPrefix+"/auth/login").permitAll()//Without authorization login
                .anyRequest().authenticated())//Protected all other endpoints
            
            .csrf((csrf) -> csrf.disable())//Disable CSRF for the all endpoints
            .build();

    }

    //Encoding the password 
    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
    
