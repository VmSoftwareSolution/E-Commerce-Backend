package ecommerce.e_commerce.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.e_commerce.permission.entity.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity,Long> {
    
}
