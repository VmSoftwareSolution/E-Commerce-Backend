package ecommerce.e_commerce.permission;

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
import ecommerce.e_commerce.permission.dto.CreatePermissionDto;
import ecommerce.e_commerce.permission.dto.PaginationPermissionDto;
import ecommerce.e_commerce.permission.dto.UpdatePermissionDto;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.permission.repository.PermissionRepository;

@Service
public class PermissionService implements PermissionServiceInterface {

    
    @Autowired
    private final PermissionRepository permissionRepository;


    PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }


    /**
    * Creating a new permission.
    * This method accept's a 'CreatePermissionDto' containing the permission details:
    * Transform the dto to {@LinK PermissionEntity} and calling {@Link PermissionRepository}
    *
    * @param createPermissionDto a {@Link CreatePermissionDto} object containing the permission details:
    *                            - name (String): the permissions's name
    *                            - description (String): the permission's description, is optional
    *
    * @Return PermissionEntity, return the already create Permission                                           
    */
    @Override
    public PermissionEntity createPermission(CreatePermissionDto createPermissionDto){
        //Mapping data
        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setName(createPermissionDto.name);
        permissionEntity.setDescription(createPermissionDto.description);

        return permissionRepository.save(permissionEntity);
    }
    
    /**
    * Retrieves a list of permissions based on the given pagination and filtering criteria.
    * 
    * This method fetches all permissions from the database and applies various filters
    * and sorting based on the provided {@link PaginationPermissionDto}.
    * 
    * The method supports:
    * - Flattening data if `flatten` is set to true (restricting additional filters).
    * - Limiting and offsetting results for pagination.
    * - Sorting results in ascending (default) or descending order based on name.
    * - Filtering by permission name if provided.
    * 
    * @param paginationPermissionDto an optional {@link PaginationPermissionDto} object 
    *                                containing pagination and filtering options.
    * 
    * @return a list of maps, each representing a permission entity with id, name, 
    *         and optionally description, structured according to the request parameters.
    * 
    * @throws IllegalArgumentException if `flatten` is true but additional filtering 
    *                                  parameters are provided.
    */
    @Override
    public List<Map<String, Object>> findPermission(PaginationPermissionDto paginationPermissionDto) {
        
        //Find all permission in the database
        List<PermissionEntity> foundPermission = permissionRepository.findAll();
        

        //Destructuring PaginationPermissionDto
        boolean flatten = paginationPermissionDto !=null && paginationPermissionDto.isFlatten();
        int limit = paginationPermissionDto !=null && paginationPermissionDto.getLimit()!=0
            ? paginationPermissionDto.getLimit():50;
        int offset = paginationPermissionDto !=null && paginationPermissionDto.getOffset()!=0
            ? paginationPermissionDto.getOffset():0;
        String sortOrder = paginationPermissionDto != null ? paginationPermissionDto.getSortOrder():null;
        String name = paginationPermissionDto !=null ? paginationPermissionDto.getName():null;

        //Valid if flatten provide
        if(flatten){
            if(flatten && (
                limit!=50 ||
                offset!= 0 ||
                sortOrder!=null ||
                name!=null
            )){
                throw new IllegalArgumentException("The paginationPermissionDto object cannot have other fields besides 'flatten'");    
            }

            Map<String,Object> response = new LinkedHashMap<>();

            response.put("Context", "Permission");
            response.put("TotalData", foundPermission.size());
            response.put("Data", foundPermission.stream()
                .map(permission ->{
                    Map<String,Object> permissionMap = new LinkedHashMap<>();

                    permissionMap.put("id", permission.getId());
                    permissionMap.put("name", permission.getName());

                    return permissionMap;
                })
                .collect(Collectors.toList()));
                return Collections.singletonList(response);
        }


        //Logic for sortORder DESC, this is default to ASC
        Comparator<Map<String,Object>> sort = Comparator.comparing(m->(String) m.get("name"));

        if(sortOrder != null && sortOrder.equalsIgnoreCase("DESC")){
            sort = sort.reversed();
        }

        //Filter based on name if provide
        Predicate<PermissionEntity> nameFilter = permission -> name == null
            || permission.getName().toLowerCase().contains(name.toLowerCase());

        //Iterate to foundPermission and extract id,name and description
        Map<String,Object> response = new LinkedHashMap<>();
        
        response.put("Context", "Permission");
        response.put("TotalData", foundPermission.size());
        response.put("Data", foundPermission.stream()
            .filter(nameFilter)
            .map(permission->{
                Map<String,Object> permissionMap = new LinkedHashMap<>();

                permissionMap.put("id", permission.getId());
                permissionMap.put("name", permission.getName());
                permissionMap.put("description", permission.getDescription());

                return permissionMap;
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
    
    /**
    * Updates an existing permission.
    * 
    * This method retrieves the permission by its ID and updates its fields
    * if the corresponding values are provided in the {@link UpdatePermissionDto}.
    * 
    * @param id The ID of the permission to update.
    * @param updatePermissionDto A DTO containing the new values for the permission.
    * 
    * @return The updated {@link PermissionEntity}.
    * 
    * @throws NoSuchElementException if the permission with the given ID is not found.
    */
    @Override
    public PermissionEntity updatePermission(Long id,UpdatePermissionDto updatePermissionDto) {

        //Find permission by id and if don't exist throw error
        PermissionEntity foundPermission = this.findByIdOrFail(id);

        Optional.ofNullable(updatePermissionDto.name)//Valid if name is not null
            .ifPresent(name ->{//valid if name has value
                foundPermission.setName(name);
            });

        Optional.ofNullable(updatePermissionDto.description)//Valid if description is not null
            .ifPresent(description->{//Valid if description has value
                foundPermission.setDescription(description);
            });

        return permissionRepository.save(foundPermission);
    }

    
    /**
    * Finds a permission by its ID.
    * 
    * This method searches for a permission entity in the database
    * and returns an {@link Optional} containing the entity if found.
    * 
    * @param id The ID of the permission to find.
    * @return An {@link Optional} of {@link PermissionEntity}.
    */
    @Override
    public Optional<PermissionEntity> findById(Long id) {
        return permissionRepository.findById(id);
    }


    /**
    * This method find by permission id and return data, if data is empty
    * @return a NoSuchElementException 
    */
    @Override
    public PermissionEntity findByIdOrFail(Long id) {
        return this.findById(id).orElseThrow(
            ()-> new NoSuchElementException("Permission with id = " + id + " not found.")
        );
    }






    
}
