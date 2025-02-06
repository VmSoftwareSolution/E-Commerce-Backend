package ecommerce.e_commerce.roles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.permission.mockData.PermissionMockData;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.roles.mockData.RolesMockData;
import ecommerce.e_commerce.roles.repository.RolesRepository;

public class RolesServiceTest {
    
    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PermissionServiceInterface permissionServiceInterface;

    @InjectMocks
    private RolesService rolesService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveRolesSuccessfully(){
        //Initialize variable
        CreateRolesDto createRolesDto 
            = RolesMockData.createRolesDto();

        //List permission Entity 
        List<PermissionEntity> permissionEntity = 
            PermissionMockData.createPermissionEntity(createRolesDto.permission.get(0));

        RolesEntity roleEntity 
            = RolesMockData.createRoleEntity(createRolesDto, permissionEntity);

        //Valid if permission id exist
        when(permissionServiceInterface
            .findByIdOrFail(createRolesDto.permission.get(0))
        ).thenReturn(Optional.of(permissionEntity.get(0)));;
        
        //Call repository
        when(rolesRepository
            .save(any(RolesEntity.class))
        ).thenReturn(roleEntity);

        //call
        RolesEntity result = rolesService.createRoles(createRolesDto);

         //Asserts
        assertEquals(createRolesDto.name, result.getName());
        assertEquals(createRolesDto.description, result.getDescription());
        assertEquals(createRolesDto.permission.get(0), result.getPermission().get(0).getId());

        //Verify
        verify(permissionServiceInterface).findByIdOrFail(createRolesDto.permission.get(0));
        verify(rolesRepository).save(any(RolesEntity    .class));

    }

    @Test
    public void testSaveRolesWithoutDescription(){
        //Initialize variable
        CreateRolesDto createRolesDto 
            = RolesMockData.createRolesDto();

         createRolesDto.description=null;

        //List permission Entity 
        List<PermissionEntity> permissionEntity = 
            PermissionMockData.createPermissionEntity(createRolesDto.permission.get(0));

        RolesEntity roleEntity 
            = RolesMockData.createRoleEntity(createRolesDto, permissionEntity);

        //Valid if permission id exist
        when(permissionServiceInterface
            .findByIdOrFail(createRolesDto.permission.get(0))
        ).thenReturn(Optional.of(permissionEntity.get(0)));;
      
        //Call repository
        when(rolesRepository
            .save(any(RolesEntity.class))
        ).thenReturn(roleEntity);

        //call
        RolesEntity result = rolesService.createRoles(createRolesDto);

        //Asserts
        assertEquals(createRolesDto.name, result.getName());
        assertEquals(createRolesDto.description, null);
        assertEquals(createRolesDto.permission.get(0), result.getPermission().get(0).getId());

        //Verify
        verify(permissionServiceInterface).findByIdOrFail(createRolesDto.permission.get(0));
        verify(rolesRepository).save(any(RolesEntity    .class));
    }

    @Test
    public void testSaveRolesAlreadyExist(){
        //Initialize variable
        CreateRolesDto createRolesDto 
            = RolesMockData.createRolesDto();

        //List permission Entity 
        List<PermissionEntity> permissionEntity = 
            PermissionMockData.createPermissionEntity(createRolesDto.permission.get(0));


        //Valid if permission id exist
        when(permissionServiceInterface
         .findByIdOrFail(createRolesDto.permission.get(0))
        ).thenReturn(Optional.of(permissionEntity.get(0)));;

        doThrow(
            new DataIntegrityViolationException("Duplicate key value violates unique constraint")
        ).when(rolesRepository).save(any(RolesEntity.class));

        try {
            rolesService.createRoles(createRolesDto);
        } catch (DataIntegrityViolationException e) {
            assertEquals("Duplicate key value violates unique constraint", e.getMessage());
        }

          // Verify repository interaction
        Mockito.verify(rolesRepository).save(any(RolesEntity.class));
    }

    @Test
    public void testSaveRolesPermissionNotFound(){
        //Initialize variable
        CreateRolesDto createRolesDto 
            = RolesMockData.createRolesDto();

        doThrow(
            new NoSuchElementException("Permission not found")
        ).when(rolesRepository).save(any(RolesEntity.class));

        try {
            rolesService.createRoles(createRolesDto);
        } catch (NoSuchElementException e) {
            assertEquals("Permission not found", e.getMessage());
        }

          // Verify repository interaction
        Mockito.verify(rolesRepository).save(any(RolesEntity.class));
    }


    @Test
    public void testSaveRolesUnexpectedError(){
        //Initialize variable
        CreateRolesDto createRolesDto 
            = RolesMockData.createRolesDto();

        //List permission Entity 
        List<PermissionEntity> permissionEntity = 
            PermissionMockData.createPermissionEntity(createRolesDto.permission.get(0));


        //Valid if permission id exist
        when(permissionServiceInterface
         .findByIdOrFail(createRolesDto.permission.get(0))
        ).thenReturn(Optional.of(permissionEntity.get(0)));;

        doThrow(
            new RuntimeException("Unexpected error occurred")
        ).when(rolesRepository).save(any(RolesEntity.class));

        // Act & Assert
        RuntimeException exception = 
            assertThrows(RuntimeException.class, () -> {
                rolesService.createRoles(createRolesDto);
        });

        assertEquals("Unexpected error occurred", exception.getMessage());

          // Verify repository interaction
        Mockito.verify(rolesRepository).save(any(RolesEntity.class));
    }
}
