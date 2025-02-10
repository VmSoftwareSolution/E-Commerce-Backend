package ecommerce.e_commerce.user;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.common.interfaces.user.UserServiceInterface;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.dto.UpdateUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserServiceInterface {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RolesServiceInterface rolesServiceInterface;


    /**
    * This method retrieves a list of users from the database with optional pagination, sorting, and filtering.
    * 
    * @param paginationUserDto The DTO containing pagination, sorting, and filtering information (email, role, flatten flag).
    * @return A list of maps, each containing user data (id, email, and role), and meta information (Context, TotalData).
    * @throws IllegalArgumentException If the flatten flag is set to true while other fields are also provided in the paginationUserDto.
    */
    @Override
    public List<Map<String, Object>> findUser(
        PaginationUserDto paginationUserDto
    ){
        //find all user in the database
        List<UserEntity> foundUser = userRepository.findAll();

        //Destructuring paginationUserDto
        boolean flatten = paginationUserDto !=null && paginationUserDto.isFlatten();
        int limit = paginationUserDto !=null && paginationUserDto.getLimit() !=0 
            ? paginationUserDto.getLimit():50;
        int offset = paginationUserDto !=null && paginationUserDto.getOffset() !=0
            ? paginationUserDto.getOffset():0;
        String sortOrder = paginationUserDto !=null ? paginationUserDto.getSortOrder():null;
        String email = paginationUserDto !=null ? paginationUserDto.getEmail():null;
        String roles = paginationUserDto !=null ? paginationUserDto.getRoles():null;

        //Valid if provide flatten
        if(flatten){
            if(flatten && (
                limit!=50 || 
                offset!=0 || 
                sortOrder!=null || 
                roles!=null ||
                email!=null )
            ){
                throw new IllegalArgumentException("The paginationUserDto object cannot have other fields besides 'flatten'");
            }

            Map<String,Object> response = new LinkedHashMap<>();
            response.put("Context", "User");
            response.put("TotalData", foundUser.size());
            response.put("Data", foundUser.stream()
            .map(user ->{
                Map<String,Object> userMap = new LinkedHashMap<>();
                
                userMap.put("id", user.getId());
                userMap.put("email", user.getEmail());

                return userMap;
            })
            .collect(Collectors.toList()));
            return Collections.singletonList(response);
        }

        //Logic for sortOrder DESC, this is default ASC
        Comparator<Map<String,Object>> sort = Comparator.comparing(m ->(String) m.get("email"));

        if(sortOrder !=null && sortOrder.equalsIgnoreCase("DESC")){
            sort = sort.reversed();
        }

        //Filter based on email or roles if provided
        Predicate<UserEntity> emailFilter = user -> email == null || user.getEmail().toLowerCase().contains(email.toLowerCase());
        Predicate<UserEntity> roleFilter = user -> roles == null || user.getRole().getName().toLowerCase().contains(roles.toLowerCase());

        
        //Iterate to foundUser and extract id, email and role(id and name)
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("Context", "User");
        response.put("TotalData", foundUser.size());
        response.put("Data", foundUser.stream()
            .filter(emailFilter.and(roleFilter))
            .map(user -> {
                Map<String,Object> userMap = new LinkedHashMap<>();

                userMap.put("id", user.getId());
                userMap.put("email", user.getEmail());

                //mapping role
                Map<String, Object> roleMap = new LinkedHashMap<>();
                roleMap.put("id", user.getRole().getId());
                roleMap.put("name", user.getRole().getName());
                
                userMap.put("role", roleMap);

                return userMap;
            })
            .sorted(sort)
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList())
        );
        
        return Collections.singletonList(response);    
    }

    /**
    * Updates a user with the data provided in the UpdateUserDto. If the email, password, or role is provided, 
    * the respective values will be updated. The method finds the user by ID, applies changes, and then saves the updated user.
    * 
    * @param id The ID of the user to be updated.
    * @param updateUserDto The DTO containing the new values for the user fields (email, password, role).
    * @return The updated UserEntity after applying the changes.
    * @throws NoSuchElementException If the user with the specified ID is not found.
    */
    @Override
    public UserEntity updateUser(Long id, UpdateUserDto updateUserDto) {

        //find user by id and if don't exist throw error
        UserEntity foundUser = this.findByIdOrFail(id);
        
        Optional.ofNullable(updateUserDto.email)//valid if email is not null
            .ifPresent(email ->{//valid if email has value
                foundUser.setEmail(email);//set value                
            });
        
        Optional.ofNullable(updateUserDto.password)//valid if password is not null
            .ifPresent(password ->{//valid if password has value
                foundUser.setPassword(encoder.encode(password));//set value with encoder 
            });

        Optional.ofNullable(updateUserDto.role)
            .ifPresent(role ->{
                RolesEntity foundRole 
                    = rolesServiceInterface.findByIdOrFail(role);//find role id, and valid if already exist

                foundUser.setRole(foundRole);//set value
            });
            
            return userRepository.save(foundUser);
        }
        
    
    /**
     * Retrieves user details by user ID and maps the relevant information.
     * <p>
     * This method finds a user by the provided ID and constructs a structured response
     * containing user details and their associated role information.
     * </p>
     *
     * @param id the unique identifier of the user to retrieve.
     * @return a {@link List} containing a single {@link Map} with the following details:
     *         - {@code id} (Long): The user's ID.
     *         - {@code name} (String): The user's email.
     *         - {@code role} (Map<String, Object>): A nested map containing:
     *             - {@code id} (Long): The role ID.
     *             - {@code name} (String): The role name.
     * 
     * @throws EntityNotFoundException if the user with the given ID is not found.
    */
    @Override
    public List<Map<String, Object>> findUserDetail(Long id) {
        //Find user by id
        UserEntity foundUser = this.findByIdOrFail(id);

        //Mapping user data
        Map<String,Object> response = new LinkedHashMap<>();

        response.put("id", foundUser.getId());
        response.put("name", foundUser.getEmail());
        
        //Mapping role data
        Map<String,Object> roleMap = new LinkedHashMap<>();

        roleMap.put("id",foundUser.getRole().getId());
        roleMap.put("name",foundUser.getRole().getName());
        
        //put roleMap to response
        response.put("role", roleMap);

        //Return response as list
        return Collections.singletonList(response);
    }

    //Bases methods

    /**
    * Finds a user by its ID. If the user is found, it returns the user as an Optional.
    * 
    * @param id The ID of the user to find.
    * @return An Optional containing the user if found, otherwise an empty Optional.
    */
    @Override
    public Optional<UserEntity> findById(Long id){
        return userRepository.findById(id);
    }

    /**
    * Finds a user by its ID and throws an exception if not found.
    * 
    * @param id The ID of the user to find.
    * @return The found user entity.
    * @throws NoSuchElementException If no user with the specified ID is found.
    */
    @Override
    public UserEntity findByIdOrFail(Long id){
        return this.findById(id).orElseThrow(
            ()-> new NoSuchElementException("User with id "+ id + " not found"));
    }


}
