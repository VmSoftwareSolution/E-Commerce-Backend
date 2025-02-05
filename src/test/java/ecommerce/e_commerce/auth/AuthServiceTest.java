package ecommerce.e_commerce.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.auth.mockData.AuthMockData;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.common.jwt.JwtService;
import ecommerce.e_commerce.roles.mockData.RolesMockData;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.repository.UserRepository;

public class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private RolesServiceInterface rolesServiceInterface;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private AuthService authService;



    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserSuccessfully(){
        //Initialize variable
        CreateUserDto createUserDto
            = AuthMockData.createUserDto();

        UserEntity userEntity
            = AuthMockData.userEntity(createUserDto);
    
        when(
            rolesServiceInterface.findRolesByNameOrFail("Guest")
        ).thenReturn(RolesMockData.rolesEntityOptionalList());

        when(
            encoder.encode(createUserDto.password)
        ).thenReturn("encoderPassword");

        when(userRepository.save(
            any(UserEntity.class))
        ).thenReturn(userEntity);

        UserEntity result = authService.createUser(createUserDto);

        //Asserts
        assertEquals(userEntity.getEmail(), result.getEmail());
        assertEquals(userEntity.getPassword(), result.getPassword());
        assertEquals("Guest", result.getRole().getName());

        //Verify
        verify(userRepository).save(any(UserEntity.class));
        verify(rolesServiceInterface).findRolesByNameOrFail("Guest");
        verify(encoder).encode(createUserDto.password);
    }

    @Test
    public void testRegisterUserRoleNotFound(){
        //Initialize variable
        CreateUserDto createUserDto
            = AuthMockData.createUserDto();

        UserEntity userEntity
            = AuthMockData.userEntity(createUserDto);
    
            
        when(
            rolesServiceInterface.findRolesByNameOrFail("Guest")
        ).thenThrow(new NoSuchElementException("Role with name = Guest not found"));
        
        when(
            encoder.encode(createUserDto.password)
        ).thenReturn("encoderPassword");

        when(userRepository.save(
            any(UserEntity.class))
        ).thenReturn(userEntity);

        try {
            authService.createUser(createUserDto);
        } catch (Exception e) {
            assertEquals("Role with name = Guest not found", e.getMessage());
        }

        //Verify
        verify(rolesServiceInterface).findRolesByNameOrFail("Guest");
        verify(encoder).encode(createUserDto.password);
    }

    @Test
    public void testRegisterUserAlreadyExist(){
        //Initialize variable
        CreateUserDto createUserDto
            = AuthMockData.createUserDto();
    
        when(
            rolesServiceInterface.findRolesByNameOrFail("Guest")
        ).thenReturn(RolesMockData.rolesEntityOptionalList());

        when(
            encoder.encode(createUserDto.password)
        ).thenReturn("encoderPassword");

        doThrow(
            new DataIntegrityViolationException("Duplicate key value violates unique constraint")   
        ).when(userRepository).save(any(UserEntity.class));

        try {
            authService.createUser(createUserDto);
        } catch (Exception e) {
            assertEquals("Duplicate key value violates unique constraint", e.getMessage());
        }

        //Verify
        verify(userRepository).save(any(UserEntity.class));
        verify(rolesServiceInterface).findRolesByNameOrFail("Guest");
        verify(encoder).encode(createUserDto.password);
    }

    @Test
    public void testRegisterUserUnexpectedError(){
        //Initialize variable
        CreateUserDto createUserDto
            = AuthMockData.createUserDto();

    
        when(
            rolesServiceInterface.findRolesByNameOrFail("Guest")
        ).thenReturn(RolesMockData.rolesEntityOptionalList());

        when(
            encoder.encode(createUserDto.password)
        ).thenReturn("encoderPassword");

        doThrow(
            new RuntimeException("Unexpected error occurred")
        ).when(userRepository).save(any(UserEntity.class));

       
        // Act & Assert
        RuntimeException exception = 
            assertThrows(RuntimeException.class, () -> {
                authService.createUser(createUserDto);
        });

        assertEquals("Unexpected error occurred", exception.getMessage());


        //Verify
        verify(userRepository).save(any(UserEntity.class));
        verify(rolesServiceInterface).findRolesByNameOrFail("Guest");
        verify(encoder).encode(createUserDto.password);
    }

        @Test
    public void testLoginUserWithInvalidCredentials() {
        // Initialize variables
        CreateUserDto createUserDto = AuthMockData.createUserDto();

        doThrow(new BadCredentialsException("Invalid credentials"))
            .when(authenticationManager)
            .authenticate(any(UsernamePasswordAuthenticationToken.class));

        BadCredentialsException exception = assertThrows(
            BadCredentialsException.class,
            () -> authService.loginUser(createUserDto)
        );

        // Asserts
        assertEquals("Invalid credentials", exception.getMessage());

        // Verify
        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(createUserDto.email, createUserDto.password)
        );
    }

    @Test
    public void testLoginUserSuccessfully() {
        // Initialize variables
        CreateUserDto createUserDto = AuthMockData.createUserDto();
        UserEntity userEntity = AuthMockData.userEntity(createUserDto);
        String expectedToken = AuthMockData.generateTokenAdmin();

        when(userRepository.findByEmail(createUserDto.email))
            .thenReturn(Optional.of(userEntity));

        when(jwtService.getToken(
                any(UserDetails.class))
            ).thenReturn(expectedToken);

        String result = authService.loginUser(createUserDto);

        // Asserts
        assertEquals(expectedToken, result);

        // Verify
        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(
                createUserDto.email,
                createUserDto.password
            ));
        verify(userRepository).findByEmail(createUserDto.email);
        verify(jwtService).getToken(userEntity);
    }

    @Test
    public void testLoginUserNotFound() {
        // Initialize variables
        CreateUserDto createUserDto = AuthMockData.createUserDto();

        when(userRepository.findByEmail(createUserDto.email))
            .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> authService.loginUser(createUserDto)
        );

        // Asserts
        assertEquals("No value present", exception.getMessage());

        // Verify
        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(
                createUserDto.email,
                createUserDto.password
            ));
        verify(userRepository).findByEmail(createUserDto.email);
    }

    @Test
    public void testLoginUserUnexpectedError() {
        // Initialize variables
        CreateUserDto createUserDto = AuthMockData.createUserDto();

        doThrow(new RuntimeException("Unexpected error occurred"))
            .when(authenticationManager)
            .authenticate(any(UsernamePasswordAuthenticationToken.class));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.loginUser(createUserDto)
        );

        // Asserts
        assertEquals("Unexpected error occurred", exception.getMessage());

        // Verify
        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(
                createUserDto.email,
                createUserDto.password
            ));
    }
}
