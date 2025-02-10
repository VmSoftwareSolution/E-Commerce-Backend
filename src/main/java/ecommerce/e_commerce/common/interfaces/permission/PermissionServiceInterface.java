package ecommerce.e_commerce.common.interfaces.permission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.dto.PaginationPermissionDto;
import ecommerce.e_commerce.permission.dto.UpdatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;

public interface PermissionServiceInterface {
    
    public PermissionEntity createPermission(
        CreatePermissionDto createPermissionDto
    );

    public List<Map<String,Object>> findPermission(
        PaginationPermissionDto paginationPermissionDto
    );

    public PermissionEntity updatePermission(
        Long id,
        UpdatePermissionDto updatePermissionDto
    );

    public List<Map<String,Object>> findPermissionDetail(
        Long id
    );

    //Base methods
    public Optional<PermissionEntity> findById(Long id);
    public PermissionEntity findByIdOrFail(Long id);
}
