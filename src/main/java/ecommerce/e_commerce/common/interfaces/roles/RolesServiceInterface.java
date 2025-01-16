package ecommerce.e_commerce.common.interfaces.roles;

import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;

public interface RolesServiceInterface {

  public RolesEntity createRoles(CreateRolesDto createRolesDto);  
} 
  

