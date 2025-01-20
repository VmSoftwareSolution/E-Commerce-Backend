package ecommerce.e_commerce.roles.mockData;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public static RolesEntity roleEntity(
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
    public static Optional<RolesEntity> rolesEntityOptionalList(){
        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setName("Guest");
        rolesEntity.setDescription("user someone permission");

        Optional<RolesEntity> optionalEntity = Optional.of(rolesEntity);

        return optionalEntity;
    }

}
