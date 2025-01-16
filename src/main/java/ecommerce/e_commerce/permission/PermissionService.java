package ecommerce.e_commerce.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.permission.repository.PermissionRepository;

@Service
public class PermissionService implements PermissionServiceInterface {
    
    @Autowired
    private PermissionRepository permissionRepository;


    /**
    * Creating a new permission.
    * This method accept's a 'CreatePermissionDto' containing the permission details:
    * Transform the dto to {@LinK PermissionEntity} and calling {@Link PermissionRepository}
    *
    * @param createPermissionDto a {@Link CreatePermissionDto} object containing the permission details:
    *                            - name (String): the permissions's name
    *                            - description (String): the permission's description, is optional
    *
    * @Return PermissionEntity, return the already create Permission                                           
    */
    @Override
    public PermissionEntity createPermission(CreatePermissionDto createPermissionDto){
        //Mapping data
        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setName(createPermissionDto.name);
        permissionEntity.setDescription(createPermissionDto.description);

        return permissionRepository.save(permissionEntity);
    }
}
