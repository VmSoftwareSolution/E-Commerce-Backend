package ecommerce.e_commerce.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.e_commerce.user.entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity,Long>{
    
    
}
