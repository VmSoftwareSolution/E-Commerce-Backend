package ecommerce.e_commerce.common.interfaces.permission;

import java.util.Optional;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;

public interface PermissionServiceInterface {
    
    public PermissionEntity createPermission(CreatePermissionDto createPermissionDto);

    //Base methods
    public Optional<PermissionEntity> findById(Long id);
    public Optional<PermissionEntity> findByIdOrFail(Long id);
}
