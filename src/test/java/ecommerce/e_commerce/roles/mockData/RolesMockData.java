package ecommerce.e_commerce.roles.mockData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;

public class RolesMockData {
    
    /**
    * Creates a DTO object for creating a new Role.
    * This DTO contains the basic information required to define a roles,
    * including its name, an optional description and permission .
    *
    *@Return a {@Link CreateRolesDto} instance pre-filled with default value 
    */
    public static CreateRolesDto createRolesDto(){
        CreateRolesDto dto = new CreateRolesDto();

        dto.name="Guest";
        dto.description="user someone permission";
        dto.permission = Arrays.asList(1L, 2L);;

        return dto;
    }



    /**
     * Creates a RolesEntity object from a CreateRolesDto and a list of PermissionEntity.
     *
     * @param dto        the {@link CreateRolesDto} containing the role details.
     * @param permission a list of {@link PermissionEntity} associated with the role.
     * @return a {@link RolesEntity} instance populated with the provided data.
     */
    public static RolesEntity createRoleEntity(
        CreateRolesDto dto,
        List<PermissionEntity> permission
    ){
        RolesEntity role = new RolesEntity();
        role.setName(dto.name);
        role.setDescription(dto.description);
        role.setPermission(permission);

        return role;
    }

    /**
     * Creates an {@link Optional} containing a {@link RolesEntity} object.
     * The {@link RolesEntity} instance is initialized with default values:
     *
     * @return an {@link Optional} containing a pre configured {@link RolesEntity} object
     */
    public static RolesEntity rolesEntityList(){
        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setId(1L);
        rolesEntity.setName("Guest");
        rolesEntity.setDescription("user someone permission");

        return rolesEntity;
    }

    /**
    * Creates a list of roles with mock data.
    *
    * @return a list of {@link Map} containing role details and associated permissions.
    */
    public static List<Map<String,Object>> RolesList(){
        // Create some mock permissions for the role
        PermissionEntity permission1 = new PermissionEntity();
        permission1.setId(1L);
        permission1.setName("read.all");
        permission1.setDescription("read all modules");
        
        PermissionEntity permission2 = new PermissionEntity();
        permission2.setId(2L);
        permission2.setName("write.all");

        List<Map<String, Object>> roles = new ArrayList<>();


        //Create some mock to roles with permission
        Map<String,Object> roles1 = new LinkedHashMap<>();

        roles1.put("id", 1L);
        roles1.put("name", "admin");
        roles1.put("description", "administrator roles");
        roles1.put("permissions", permission1);


        Map<String,Object> roles2 = new LinkedHashMap<>();

        roles2.put("id", 2L);
        roles2.put("name", "guest");
        roles2.put("description", "guest roles");
        roles2.put("permissions", permission2);

        roles.add(roles1);
        roles.add(roles2);

        return roles;
    }

    /**
    * Converts the list of roles from Map representation to RolesEntity objects.
    *
    * @return a list of {@link RolesEntity} populated with mock role data.
    */
    @SuppressWarnings("unchecked")
    public static List<RolesEntity> RolesListRepository(){
        return RolesMockData.RolesList().stream().map(rolesMap ->{
            RolesEntity roles = new RolesEntity();
            roles.setId(1L);
            roles.setName("Admin");
            roles.setDescription("Administrator roles");
            roles.setPermission((List<PermissionEntity>) rolesMap.get("permission"));
            return roles;
        }).collect(Collectors.toList());
    }

}
