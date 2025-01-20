package ecommerce.e_commerce.roles.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.e_commerce.roles.entity.RolesEntity;

public interface RolesRepository extends JpaRepository<RolesEntity,Long>{

    Optional<RolesEntity> findRolesByName(String name);
} 
    

