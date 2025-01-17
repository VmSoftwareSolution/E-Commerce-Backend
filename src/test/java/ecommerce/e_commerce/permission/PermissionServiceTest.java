package ecommerce.e_commerce.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
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
    public void testSavePermissionWithDescription(){
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

}
