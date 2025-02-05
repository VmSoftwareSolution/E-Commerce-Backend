package ecommerce.e_commerce.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ecommerce.e_commerce.common.jwt.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authProvider;

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
            .csrf(csrf ->
                csrf.disable())
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/swagger-ui/**", apiPrefix+"/v3/api-docs/**").permitAll()//Without authorization swagger
                .requestMatchers(apiPrefix+"/auth/**").permitAll()//Without authorization register
                .anyRequest().authenticated())//Protected all other endpoints
            .sessionManagement(sessionManager->
                sessionManager 
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }   
}
    
