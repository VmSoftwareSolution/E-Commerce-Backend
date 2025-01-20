package ecommerce.e_commerce.common.interfaces.auth;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;


public interface AuthServiceInterface {

  public UserEntity createUser(CreateUserDto createUser);  
} 
