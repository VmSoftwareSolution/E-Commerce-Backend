package ecommerce.e_commerce.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.roles.repository.RolesRepository;

@Service
public class RolesService implements RolesServiceInterface {

    @Autowired
    private RolesRepository rolesRepository;
    
    /**
    * Creating a new roles.
    * This method accept's a 'CreateRolesDto' containing the roles details:
    * Transform the dto to {@Link RolesEntity} and calling {@link RolesRepository} 
    *
    * @param createRolesDto a {@Link CreateRolesDto} object containing the roles details:
    *                       - name (String): The role's name
    *                       - description (String): The roles's description, is optional
    *                       - permission (Integer): The roles's permission id (e.g., 1 for create.all, 2 read.all)
    *
    * @return RolesEntity, return the already created roles
    */
    @Override
    public RolesEntity createRoles(CreateRolesDto createRolesDto) {
        //Mapping dto to entity
        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setName(createRolesDto.name);
        rolesEntity.setDescription(createRolesDto.description);
        rolesEntity.setPermission(createRolesDto.permission);

        return rolesRepository.save(rolesEntity);
    }


    
}
