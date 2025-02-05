package ecommerce.e_commerce.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.e_commerce.common.interfaces.user.UserServiceInterface;
import ecommerce.e_commerce.user.dto.PaginationUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;
import ecommerce.e_commerce.user.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface {
    
    @Autowired
    private UserRepository userRepository;

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

        List<Map<String,Object>> result = new ArrayList<>();
        result.add(response);
        
        return result;    
    }

}
