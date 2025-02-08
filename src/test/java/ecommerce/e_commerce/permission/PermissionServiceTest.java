package ecommerce.e_commerce.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.dto.PaginationPermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.permission.mockData.PermissionMockData;
import ecommerce.e_commerce.permission.repository.PermissionRepository;

public class PermissionServiceTest {
    
    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePermissionSuccessfully(){
        //Initialize variable
        CreatePermissionDto createPermissionDto 
            = PermissionMockData.createPermissionDto();

        PermissionEntity permissionEntity
            = PermissionMockData.permissionEntity(createPermissionDto);

        when(permissionRepository.save(
            any(PermissionEntity.class))
        ).thenReturn(permissionEntity);

        //Call method service
        PermissionEntity result = permissionService.createPermission(createPermissionDto);


        //Asserts
        assertEquals(permissionEntity.getName(), result.getName());
        assertEquals(permissionEntity.getDescription(), result.getDescription());

        //Verify
        verify(permissionRepository).save(any(PermissionEntity.class));
    }

    @Test
    public void testSavePermissionWithoutDescription(){
        //Initialize variable
        CreatePermissionDto createPermissionDto 
            = PermissionMockData.createPermissionDto();

            createPermissionDto.description=null;//Delete description

        PermissionEntity permissionEntity
            = PermissionMockData.permissionEntity(createPermissionDto);

        when(permissionRepository.save(
            any(PermissionEntity.class))
        ).thenReturn(permissionEntity);

        //Call method service
        PermissionEntity result = permissionService.createPermission(createPermissionDto);


        //Asserts
        assertEquals(permissionEntity.getName(), result.getName());
        assertEquals(permissionEntity.getDescription(), null);

        //Verify
        verify(permissionRepository).save(any(PermissionEntity.class));
    }

    @Test
    public void testAlreadyExist() {
        // Initialize variables
        CreatePermissionDto createPermissionDto 
            = PermissionMockData.createPermissionDto();

        // Simulate DataIntegrityViolationException
        doThrow(
            new DataIntegrityViolationException("Duplicate key value violates unique constraint")
        ).when(permissionRepository).save(any(PermissionEntity.class));

        try {
            permissionService.createPermission(createPermissionDto);
        } catch (DataIntegrityViolationException ex) {
            assertEquals("Duplicate key value violates unique constraint", ex.getMessage());
        }

        // Verify repository interaction
        Mockito.verify(permissionRepository).save(any(PermissionEntity.class));
    }

    @Test
    public void testUnexpectedError() {
        // Initialize variable
        CreatePermissionDto createPermissionDto 
            = PermissionMockData.createPermissionDto();

        // Simulate unexpected error
        doThrow(
            new RuntimeException("Unexpected error occurred")
        ).when(permissionRepository).save(any(PermissionEntity.class));

        // Act & Assert
        RuntimeException exception = 
            assertThrows(RuntimeException.class, () -> {
                permissionService.createPermission(createPermissionDto);
        });

        assertEquals("Unexpected error occurred", exception.getMessage());

        verify(permissionRepository).save(any(PermissionEntity.class));
    }

    @Test
    public void testFindPermissionWithoutPagination(){
        //Configure method when called
        when(permissionRepository.findAll())
            .thenReturn(PermissionMockData.permissionListRepository());

        //Create object to pagination
        PaginationPermissionDto paginationPermissionDto = new PaginationPermissionDto();

        //Call method findPermission
        List<Map<String,Object>> result 
            = permissionService.findPermission(paginationPermissionDto);

        //Asserts
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
            "Permission",
            result.get(0).get("Context")
        );
        assertEquals(
            PermissionMockData.permissionListRepository().size(),
            result.get(0).get("TotalData")
        );

        //Verify
        verify(permissionRepository,times(1)).findAll();
    }

    @Test
    public void testFindPermissionWithFlatten(){
        //Configure method when called
        when(permissionRepository.findAll())
            .thenReturn(PermissionMockData.permissionListRepository());

        //Create object to pagination with flatten = true
        PaginationPermissionDto paginationPermissionDto = new PaginationPermissionDto();

        //Call method find permission
        List<Map<String,Object>> result =
            permissionService.findPermission(paginationPermissionDto);

        //Asserts
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
            "Permission", 
            result.get(0).get("Context")
        );
        assertEquals(
            PermissionMockData.permissionListRepository().size(), 
            result.get(0).get("TotalData")
        );

        //Verify
        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    public void testFindPermissionSortingAndPagination(){
        //Configure method when called
        when(permissionRepository.findAll())
            .thenReturn(PermissionMockData.permissionListRepository());

        //Create object to pagination with sortOrder and limit
        PaginationPermissionDto paginationPermissionDto = new PaginationPermissionDto();

        paginationPermissionDto.setLimit(2);
        paginationPermissionDto.setSortOrder("DESC");

        //Call method find permission
        List<Map<String,Object>> result 
            = permissionService.findPermission(paginationPermissionDto);

        //Asserts
        assertNotNull(result);
        assertEquals(
            1,
            ((List<?>)  result.get(0).get("Data")).size()
        );

        //Verify
        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    public void testFindPermissionWithNameFilter(){
        //Configure method when called
        when(permissionRepository.findAll())
            .thenReturn(PermissionMockData.permissionListRepository());

        //Create object to pagination with name filter
        PaginationPermissionDto paginationPermissionDto = new PaginationPermissionDto();
        paginationPermissionDto.setName("write.all");

        //Call method find permission
        List<Map<String,Object>> result 
        = permissionService.findPermission(paginationPermissionDto);

        List<?> data = (List<?>) result.get(0).get("Data");

        //Asserts
        assertNotNull(result);
        assertEquals(1, data.size());

        //Verify
        verify(permissionRepository,times(1)).findAll();
    }

    @Test
    public void testFindPermissionWithInvalidFLatten(){
        //Configure method when called
        when(permissionRepository.findAll())
            .thenReturn(PermissionMockData.permissionListRepository());

        //Create objet to pagination with flatten and limit
        PaginationPermissionDto paginationPermissionDto = new PaginationPermissionDto();

        paginationPermissionDto.setFlatten(true);
        paginationPermissionDto.setLimit(5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.findPermission(paginationPermissionDto);
        });
        
        //Asserts
        assertEquals(
            "The paginationPermissionDto object cannot have other fields besides 'flatten'", 
            exception.getMessage()
        );
        
        //Verify
        verify(permissionRepository).findAll();
    }



}
