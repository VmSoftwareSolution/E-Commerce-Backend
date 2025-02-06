package ecommerce.e_commerce.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.roles.mockData.RolesMockData;
import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.dto.UpdateUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.mockData.UserMockData;
import ecommerce.e_commerce.user.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolesServiceInterface rolesServiceInterface;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testFindUserWithoutPagination(){
        when(userRepository.findAll())
            .thenReturn(UserMockData.UserListRepository());

        PaginationUserDto paginationUserDto = new PaginationUserDto();

        List<Map<String, Object>> result 
            = userService.findUser(paginationUserDto);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
            "User", 
            result.get(0).get("Context")
        );
        assertEquals(
            UserMockData.UserListRepository().size(),
            result.get(0).get("TotalData")
        );

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserWithFlattenTrue() {
        when(userRepository.findAll())
            .thenReturn(UserMockData.UserListRepository());
        
        PaginationUserDto paginationUserDto = new PaginationUserDto();
        paginationUserDto.setFlatten(true);
        
        List<Map<String, Object>> result 
            = userService.findUser(paginationUserDto);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
            "User", 
            result.get(0).get("Context")
        );
        assertEquals(
            UserMockData.UserListRepository().size(), 
            result.get(0).get("TotalData")
        );

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserWithSortingAndPagination() {
        when(userRepository.findAll())
            .thenReturn(UserMockData.UserListRepository());

        PaginationUserDto paginationUserDto = new PaginationUserDto();
        paginationUserDto.setSortOrder("DESC");
        paginationUserDto.setLimit(1);
        paginationUserDto.setOffset(0);
        
        List<Map<String, Object>> result 
            = userService.findUser(paginationUserDto);
        
        assertNotNull(result);
        assertEquals(
            1, 
            ((List<?>) result.get(0).get("Data")).size()
        );

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserWithEmailFilter() {
        when(userRepository.findAll())
            .thenReturn(UserMockData.UserListRepository());
        
        PaginationUserDto paginationUserDto = new PaginationUserDto();
        paginationUserDto.setEmail("user1@example.com");
        
        List<Map<String, Object>> result 
            = userService.findUser(paginationUserDto);
        
        List<?> data = (List<?>) result.get(0).get("Data");
        
        assertNotNull(result);
        assertEquals(1, data.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserWithRoleFilter() {
        when(userRepository.findAll())
            .thenReturn(UserMockData.UserListRepository());
        
        PaginationUserDto paginationUserDto = new PaginationUserDto();
        paginationUserDto.setRoles("Admin");
        
        List<Map<String, Object>> result 
            = userService.findUser(paginationUserDto);
        
            List<?> data = (List<?>) result.get(0).get("Data");
        
        assertNotNull(result);
        assertEquals(2, data.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUser_WithInvalidFlatten_ShouldThrowIllegalArgumentException() {
        when(userRepository.findAll())
            .thenReturn(UserMockData.UserListRepository());
        
        PaginationUserDto paginationUserDto = new PaginationUserDto();
        paginationUserDto.setFlatten(true);
        paginationUserDto.setLimit(10);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUser(paginationUserDto);
        });
        
        assertEquals(
            "The paginationUserDto object cannot have other fields besides 'flatten'", 
            exception.getMessage()
        );
        
        verify(userRepository).findAll();
    }

    @Test
    public void testUpdateUserSuccessfully(){
        //Initialize variable
        Long id = 2L;
        UpdateUserDto updateUserDto = UserMockData.updateUserDto();


        //Configure methods when called
        when(userRepository.findById(id))
            .thenReturn(Optional.of(UserMockData.updateUserEntity()));

        when(encoder.encode(updateUserDto.password))
            .thenReturn("encodePassword");

        when(rolesServiceInterface.findByIdOrFail(updateUserDto.role))
            .thenReturn(RolesMockData.rolesEntityList());

        when(userRepository.save(any(UserEntity.class)))
            .thenReturn(UserMockData.updateUserEntity());

        UserEntity result = userService.updateUser(id, updateUserDto);

        //Asserts
        assertEquals(updateUserDto.email, result.getEmail());
        assertEquals(updateUserDto.password, result.getPassword());
        assertEquals(updateUserDto.role, result.getRole().getId());

        //Verify
        verify(userRepository).findById(id);
        verify(encoder).encode(updateUserDto.password);
        verify(rolesServiceInterface).findByIdOrFail(updateUserDto.role);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testUpdateUserNotFound(){
        //Initialize variable
        Long id = 1L;
        UpdateUserDto updateUserDto = UserMockData.updateUserDto();

        //COnfigure method when called
        when(userRepository.findById(id))
            .thenThrow(new NoSuchElementException("User with id " + id + " not found.")
            );

        //Assert
        assertThrows(
            NoSuchElementException.class,
            ()-> userService.updateUser(id, updateUserDto)
        );

        //Verify
        verify(userRepository).findById(id);
    }

    @Test
    public void testUpdateUserRoleNotFount(){
        Long id = 1L;
        UpdateUserDto updateUserDto = UserMockData.updateUserDto();

        when(userRepository.findById(id))
            .thenReturn(Optional.of(UserMockData.updateUserEntity()));

        when(encoder.encode(updateUserDto.password))
            .thenReturn("encodePassword");

        when(rolesServiceInterface.findByIdOrFail(updateUserDto.role))
            .thenThrow(
                new NoSuchElementException("Roles with id = " + updateUserDto.role + " not found")
            );
        
        //Asserts
        assertThrows(
            NoSuchElementException.class,
            ()-> userService.updateUser(id, updateUserDto)
        );

        //Verify
        verify(userRepository).findById(id);
        verify(encoder).encode(updateUserDto.password);
        verify(rolesServiceInterface).findByIdOrFail(updateUserDto.role);
    }
}
