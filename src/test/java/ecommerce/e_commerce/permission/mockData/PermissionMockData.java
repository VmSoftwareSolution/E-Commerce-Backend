package ecommerce.e_commerce.permission.mockData;

import java.util.ArrayList;
import java.util.List;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;

public class PermissionMockData {

    /**
     * Creates a DTO object for creating a new permission.
     * This DTO contains the basic information required to define a permission,
     * including its name and an optional description.
     *
     * @return a {@link CreatePermissionDto} instance pre-filled with default values:
     *         - name: "write.all"
     *         - description: "create and update in the module project"
     */
    public static CreatePermissionDto createPermissionDto(){
        CreatePermissionDto dto = new CreatePermissionDto(); 
        dto.name="write.all";
        dto.description="create and update in the module project";
        
        return dto;
    } 

    /**
     * Converts a {@link CreatePermissionDto} into a {@link PermissionEntity}.
     * This method initializes a {@link PermissionEntity} instance using the data
     * provided by the DTO and assigns a default ID.
     *
     * @param createPermissionDto the DTO containing permission data, including:
     *                            - name (String): the permission's name.
     *                            - description (String): the permission's description.
     * @return a {@link PermissionEntity} instance with the following fields:
     *         - id: 1L (default value for demonstration purposes)
     *         - name: the name from the DTO.
     *         - description: the description from the DTO.
     */
    public static PermissionEntity permissionEntity(
        CreatePermissionDto createPermissionDto
    ){
        PermissionEntity entity = new PermissionEntity();
        entity.setId(1L);
        entity.setName(createPermissionDto.name);
        entity.setDescription(createPermissionDto.description);

        return entity;
    }

    /**
    * Creates a list of {@link PermissionEntity} with a single permission object.
    * This method generates a permission entity using the provided ID, and assigns
    * default values for name and description.
    *
    * @param permissionId the ID to assign to the created permission entity.
    * @return a list containing one {@link PermissionEntity} instance with the provided ID.
    */
    public static List<PermissionEntity> createPermissionEntity(Long permissionId){
       // Create a new PermissionEntity object
        PermissionEntity permission = new PermissionEntity();
        permission.setId(permissionId); // Set the ID
        permission.setName("USER.WRITE.ALL");
        permission.setDescription("create and update all user");

        // Create a list and add the permission entity to it
        List<PermissionEntity> permissions = new ArrayList<>();
        permissions.add(permission);

        return permissions; // Return the list of permissions
    }
}
