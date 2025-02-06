package ecommerce.e_commerce.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.roles.repository.RolesRepository;

@Service
public class RolesService implements RolesServiceInterface {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PermissionServiceInterface permissionServiceInterface;
    
    /**
    * Creating a new roles.
    * This method accept's a 'CreateRolesDto' containing the roles details:
    * Transform the dto to {@Link RolesEntity} and calling {@link RolesRepository} 
    *
    * @param createRolesDto a {@Link CreateRolesDto} object containing the roles details:
    *                       - name (String): The role's name
    *                       - description (String): The roles's description, is optional
    *                       - permission (List<Long>): The roles's permission id (e.g., 1 for create.all, 2 read.all)
    *
    * @return RolesEntity, return the already created roles
    */
    @Override
    public RolesEntity createRoles(CreateRolesDto createRolesDto) {
        //Mapping dto to entity
        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setName(createRolesDto.name);
        rolesEntity.setDescription(createRolesDto.description);

        //Find the permission and valid if exist
        List<PermissionEntity> foundPermission = new ArrayList<>();

        createRolesDto.permission.stream()
            .map(permission -> permissionServiceInterface.findByIdOrFail(permission)) // find permission by id
            .distinct()// ignore item duplicate
            .forEach(permission -> permission.ifPresent(foundPermission::add));// if exit add to list foundPermission

        rolesEntity.setPermission(foundPermission);

        return rolesRepository.save(rolesEntity);
    }

    //Bases methods
    
    /**
    * Finds a role by its name and returns the result or null if not found.
    * 
    * @param name The name of the role to find.
    * @return An Optional containing the role if found, otherwise an empty Optional.
    */
    @Override
    public Optional<RolesEntity> findRolesByName(String name){
        return rolesRepository.findRolesByName(name);
    }

    /**
    * This method find by roles name and return data, but if data is empty
    * @return a NoSuchElementException
    */
    //FIX: Changed this method, using orElseThrow
    @Override
    public Optional<RolesEntity> findRolesByNameOrFail(String name){
        Optional<RolesEntity> foundROles = this.findRolesByName(name);

        if(foundROles.isEmpty()){
            throw  new NoSuchElementException("Roles with name = " + name + " not found.");
        }

        return foundROles;
    }

    /**
    * Finds a role by its ID.
    * 
    * @param id The ID of the role to find.
    * @return An Optional containing the role if found, otherwise an empty Optional.
    */
    @Override
    public Optional<RolesEntity> findById(Long id) {
        return rolesRepository.findById(id);
    }

    /**
    * Finds a role by its ID and throws an exception if not found.
    * 
    * @param id The ID of the role to find.
    * @return The found role entity.
    * @throws NoSuchElementException If no role with the specified ID is found.
    */
    @Override
    public RolesEntity findByIdOrFail(Long id) {
        return this.findById(id)
            .orElseThrow(()-> new NoSuchElementException("Role with id "+ id + " not found"));
    }

    
}
