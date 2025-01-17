package ecommerce.e_commerce.permission.mockData;

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
}
