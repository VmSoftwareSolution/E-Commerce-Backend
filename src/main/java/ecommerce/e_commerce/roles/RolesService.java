package ecommerce.e_commerce.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.roles.dto.CreateRolesDto;
import ecommerce.e_commerce.roles.dto.PaginationRolesDto;
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
            .forEach(foundPermission::add);// if exit add to list foundPermission

        rolesEntity.setPermission(foundPermission);

        return rolesRepository.save(rolesEntity);
    }

    /**
    * Retrieves a list of roles based on the provided pagination and filtering criteria.
    * 
    * This method fetches all roles from the database and applies optional filters such as:
    * - Flattening the response (if `flatten` is true, only `id` and `name` are returned).
    * - Limiting and offsetting the results for pagination.
    * - Sorting the roles alphabetically in ascending or descending order.
    * - Filtering by name if provided.
    * 
    * @param paginationRolesDto A DTO containing pagination and filtering parameters:
    *        - `flatten` (boolean): If true, returns a simplified response with only `id` and `name`.
    *        - `limit` (int): Number of roles to return (default: 50).
    *        - `offset` (int): Number of roles to skip before starting to collect results (default: 0).
    *        - `sortOrder` (String): "ASC" or "DESC" sorting order based on role name (default: ASC).
    *        - `name` (String): Filter roles that contain this name (case-insensitive).
    * 
    * @return A list of maps containing roles data. If `flatten` is true, only `id` and `name` are included.
    *         Otherwise, the response includes `id`, `name`, `description`, and `permission`.
    * @throws IllegalArgumentException If `flatten` is true but other pagination parameters are also set.
    */
    @Override
    public List<Map<String, Object>> findRoles(PaginationRolesDto paginationRolesDto) {

        //Find all roles in the database
        List<RolesEntity> foundRoles = rolesRepository.findAll();

        //Destructuring to paginationRolesDto
        boolean flatten = paginationRolesDto !=null && paginationRolesDto.isFlatten();
        int limit = paginationRolesDto !=null && paginationRolesDto.getLimit()!=0
            ? paginationRolesDto.getLimit():50;
        int offset = paginationRolesDto !=null && paginationRolesDto.getOffset()!=0
            ? paginationRolesDto.getOffset():0;
        String sortOrder = paginationRolesDto !=null ? paginationRolesDto.getSortOrder():null;
        String name = paginationRolesDto !=null ? paginationRolesDto.getName():null;

        //Valid if provide flatten
        if(flatten){
            if(flatten && (
                limit!=50 ||
                offset!=0 ||
                sortOrder!=null ||
                name!=null
            )){
                throw new IllegalArgumentException("The paginationRolesDto object cannot have other fields besides 'flatten'");
            }

            Map<String,Object> response = new LinkedHashMap<>();
            response.put("Context", "Roles");
            response.put("TotalData", foundRoles.size());
            response.put("Data", foundRoles.stream()
                .map(roles ->{
                    Map<String,Object> rolesMap = new LinkedHashMap<>();
                    response.put("id", roles.getId());
                    response.put("name", roles.getName());

                    return rolesMap;
                })
                .collect(Collectors.toList()));
                return Collections.singletonList(response);
        }

        //Logic for sortORder DESC, this is default to ASC
        Comparator<Map<String,Object>> sort = Comparator.comparing(m ->(String) m.get("name"));

        if(sortOrder != null && sortOrder.equalsIgnoreCase("DESC")){
            sort = sort.reversed();
        }

        //Filter based on name if provided
        Predicate<RolesEntity> nameFilter = roles -> name == null 
            || roles.getName().toLowerCase().contains(name.toLowerCase());

        //Iterate to foundRoles and extract id,name,description and permission (id,name)
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("Context", "Roles");
        response.put("TotalData", foundRoles.size());
        response.put("Data", foundRoles.stream()
            .filter(nameFilter)
            .map(roles->{
                Map<String,Object> rolesMap = new LinkedHashMap<>();
                rolesMap.put("id", roles.getId());
                rolesMap.put("name", roles.getName());
                rolesMap.put("description", roles.getDescription());
                rolesMap.put("permission", roles.getPermission());

                return rolesMap;
            })
            .skip(offset)
            .limit(limit)
            .sorted(sort)
            .collect(Collectors.toList())
        );

        List<Map<String,Object>> result = new LinkedList<>();
        result.add(response);

        return result;
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
    @Override
    public RolesEntity findRolesByNameOrFail(String name){
        return this.findRolesByName(name)
            .orElseThrow(()-> 
                new NoSuchElementException("Roles with name = " + name + " not found.")
            );
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
