package ecommerce.e_commerce.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.mockData.UserMockData;
import ecommerce.e_commerce.user.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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
}
