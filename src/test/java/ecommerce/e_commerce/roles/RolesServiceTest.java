package ecommerce.e_commerce.roles;

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
import ecommerce.e_commerce.roles.dto.PaginationRolesDto;
import ecommerce.e_commerce.roles.dto.UpdateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.roles.mockData.RolesMockData;
import ecommerce.e_commerce.roles.repository.RolesRepository;
import ecommerce.e_commerce.user.mockData.UserMockData;

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
        ).thenReturn(permissionEntity.get(0));;
        
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
        ).thenReturn(permissionEntity.get(0));;
      
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
        ).thenReturn(permissionEntity.get(0));;

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
        ).thenReturn(permissionEntity.get(0));;

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

    @Test
    public void testFindRolesWithoutPagination(){
        //Configure method when called
        when(rolesRepository.findAll())
            .thenReturn(RolesMockData.RolesListRepository());

        //Create object pagination
        PaginationRolesDto paginationRolesDto = new PaginationRolesDto();

        //Call method findRoles
        List<Map<String,Object>> result 
            = rolesService.findRoles(paginationRolesDto);

        //Asserts
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
            "Roles", 
            result.get(0).get("Context")
        );
        assertEquals(
            UserMockData.UserListRepository().size(),
            result.get(0).get("TotalData")
        );

        //verify
        verify(rolesRepository, times(1)).findAll();
    }

    @Test
    public void testFIndROlesWhitFlatten(){
        //Configure method when called
        when(rolesRepository.findAll())
            .thenReturn(RolesMockData.RolesListRepository());

        //Create object pagination with flatten true
        PaginationRolesDto paginationRolesDto = new PaginationRolesDto();
        paginationRolesDto.setFlatten(true);

        //Call method findRoles
        List<Map<String,Object>> result 
            = rolesService.findRoles(paginationRolesDto);

        //Asserts
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
            "Roles",
            result.get(0).get("Context")
        );
        assertEquals(
            RolesMockData.RolesListRepository().size(),
            result.get(0).get("TotalData")
        );

        //Verify
        verify(rolesRepository, times(1)).findAll();
    }

    @Test
    public void testFindRolesWithSortingAndPagination(){
        //Configure method when called
        when(rolesRepository.findAll())
            .thenReturn(RolesMockData.RolesListRepository());

        //Create object pagination with sortORder, limit and offset
        PaginationRolesDto paginationRolesDto = new PaginationRolesDto();

        paginationRolesDto.setLimit(2);
        paginationRolesDto.setOffset(1);
        paginationRolesDto.setSortOrder("DESC");

        //Call method findRoles
        List<Map<String,Object>> result 
            = rolesService.findRoles(paginationRolesDto);

        //Asserts
        assertNotNull(result);
        assertEquals(
            1, 
            ((List<?>) result.get(0).get("Data")).size()
        );

        //Verify
        verify(rolesRepository, times(1)).findAll();
    }

    @Test
    public void testFindRolesWithNameFilter(){
        //Configure method when called
        when(rolesRepository.findAll())
            .thenReturn(RolesMockData.RolesListRepository());

        //Create object pagination with name
        PaginationRolesDto paginationRolesDto = new PaginationRolesDto();
        paginationRolesDto.setName("Admin");

        //Call method findRoles
        List<Map<String,Object>> result 
            = rolesService.findRoles(paginationRolesDto);

            List<?> data = (List<?>) result.get(0).get("Data");

        //Asserts
        assertNotNull(result);
        assertEquals(2, data.size());

        //Verify
        verify(rolesRepository,times(1)).findAll();
    }

    @Test
    public void testFindRoleWithInvalidFlatten(){
        //Configure methods when called
        when(rolesRepository.findAll())
            .thenReturn(RolesMockData.RolesListRepository());

        //Create object pagination with flatten and limit
        PaginationRolesDto paginationRolesDto = new PaginationRolesDto();
        paginationRolesDto.setFlatten(true);
        paginationRolesDto.setLimit(5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rolesService.findRoles(paginationRolesDto);
        });
        
        //Asserts
        assertEquals(
            "The paginationRolesDto object cannot have other fields besides 'flatten'", 
            exception.getMessage()
        );
        
        //Verify
        verify(rolesRepository).findAll();
    }

    @Test
    public void testUpdateRolesSuccessfully(){
        //Initialize variable
        Long id = 1L;
        UpdateRolesDto dto = RolesMockData.updateRolesDto();

        //Configure method when called
        when(rolesRepository.findById(id))
            .thenReturn(Optional.of(RolesMockData.updateRolesEntity()));

        when(permissionServiceInterface.findByIdOrFail(dto.permission.get(0)))
            .thenReturn(PermissionMockData.permissionEntity());

        when(rolesRepository.save(any(RolesEntity.class)))
            .thenReturn(RolesMockData.updateRolesEntity());

        //Call method updateRoles
        RolesEntity result 
            = rolesService.updateRoles(id, dto);

        //Asserts
        assertEquals(dto.name, result.getName());
        assertEquals(dto.description, result.getDescription());
        assertEquals(dto.permission.get(0), result.getPermission().get(0).getId());
        
        //Verify
        verify(rolesRepository).findById(id);
        verify(permissionServiceInterface).findByIdOrFail(dto.permission.get(0));
        verify(rolesRepository).save(any(RolesEntity.class));
    }

    @Test
    public void testUpdateRolesNotFound(){
        //Initialize variable
        Long id = 1L;
        UpdateRolesDto dto = RolesMockData.updateRolesDto();
 
        //Configure method when called
        when(rolesRepository.findById(id))
             .thenThrow(new NoSuchElementException("Roles with id " + id + " not found.")
            );

        //Asserts
        assertThrows(
            NoSuchElementException.class, 
            ()-> rolesService.updateRoles(id, dto)
        );

        //Verify
        verify(rolesRepository).findById(id);
    }

    @Test
    public void testUpdateRolesPermissionNotFound(){
        //Initialize variable
        Long id = 1L;
        UpdateRolesDto dto = RolesMockData.updateRolesDto();

        //Configure method when called
        when(rolesRepository.findById(id))
            .thenReturn(Optional.of(RolesMockData.updateRolesEntity()));

        when(permissionServiceInterface.findByIdOrFail(dto.permission.get(0)))
            .thenThrow(new NoSuchElementException("Permission not found"));

        //Asserts
        assertThrows(
            NoSuchElementException.class,
            ()-> rolesService.updateRoles(id, dto)
        );

        //Verify
        verify(rolesRepository).findById(id);
        verify(permissionServiceInterface).findByIdOrFail(dto.permission.get(0));
    }

    @Test
    public void testFindRolesDetailSuccessfully(){
        //Initialize variable
        Long id = 1L;

        //Configure method when called
        when(rolesRepository.findById(id))
            .thenReturn(Optional.of(RolesMockData.RoleEntityListDetail()));

        

        //Call method findRolesDetail
        List<Map<String,Object>> result = rolesService.findRolesDetail(id);

        //Asserts
        assertNotNull(result);
        assertEquals(1L, result.get(0).get("id"));
        assertEquals("Admin", result.get(0).get("name"));
        assertEquals("Administrator role", result.get(0).get("description"));

        //Verify
        verify(rolesRepository).findById(id);
    }

    @Test
    public void testFindRolesDetailNotFound(){
        //Initialize variable
        Long id = 1L;

        //Configure method when called
        when(rolesRepository.findById(id))
            .thenThrow(
                new NoSuchElementException("Roles with id = " + id + " not found")
            );

        //Asserts
        assertThrows(
            NoSuchElementException.class,
            ()-> rolesService.findRolesDetail(id)
        );

        //Verify
        verify(rolesRepository).findById(id);
    }
}
