package ecommerce.e_commerce.common.interfaces.permission;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;

public interface PermissionServiceInterface {
    
    public PermissionEntity createPermission(CreatePermissionDto createPermissionDto);
}
