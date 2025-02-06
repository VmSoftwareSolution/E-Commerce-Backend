package ecommerce.e_commerce.common.interfaces.roles;

import java.util.Optional;

import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;

public interface RolesServiceInterface {

  public RolesEntity createRoles(CreateRolesDto createRolesDto);  

  //Bases methods
  public Optional<RolesEntity> findRolesByName(String name);
  public RolesEntity findRolesByNameOrFail(String name);
  public Optional<RolesEntity> findById(Long id);
  public RolesEntity findByIdOrFail(Long id);
} 
  

