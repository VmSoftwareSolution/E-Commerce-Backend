package ecommerce.e_commerce.user.mockData;

import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.roles.mockData.RolesMockData;
import ecommerce.e_commerce.user.dto.UpdateUserDto;
import ecommerce.e_commerce.user.entity.UserEntity;

import java.util.*;
import java.util.stream.Collectors;


public class UserMockData {

    /**
     * Creates a list of mock data for users as Map<String, Object>.
     *
     * @return a List of Map<String, Object> containing mock user data
     */
    public static List<Map<String, Object>> UserList() {
        // Create mock roles
        RolesEntity role = new RolesEntity();
        role.setId(1L);
        role.setName("Admin");
        role.setDescription("Administrator role with full permissions");

        // Create some mock permissions for the role
        PermissionEntity permission1 = new PermissionEntity();
        permission1.setId(1L);
        permission1.setName("read.all");

        PermissionEntity permission2 = new PermissionEntity();
        permission2.setId(2L);
        permission2.setName("write.all");

        role.setPermission(Arrays.asList(permission1, permission2));

        // Creating a list of user data as Map<String, Object>
        List<Map<String, Object>> users = new ArrayList<>();

        // Create User 1
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", 1L);
        user1.put("email", "user1@example.com");
        user1.put("role", role);
        
        // Create User 2
        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", 2L);
        user2.put("email", "user2@example.com");
        user2.put("role", role);

        // Add users to the list
        users.add(user1);
        users.add(user2);

        return users;
    }

    /**
     * Creates a list of mock data for users as Map<String, Object>.
     *
     * @return a List of Map<String, Object> containing mock user data
    */
    public static List<Map<String, Object>> UserListDetail() {
        // Create mock roles
        RolesEntity role = new RolesEntity();
        role.setId(1L);
        role.setName("Admin");
        role.setDescription("Administrator role with full permissions");

        // Create some mock permissions for the role
        PermissionEntity permission1 = new PermissionEntity();
        permission1.setId(1L);
        permission1.setName("read.all");

        PermissionEntity permission2 = new PermissionEntity();
        permission2.setId(2L);
        permission2.setName("write.all");

        role.setPermission(Arrays.asList(permission1, permission2));

        // Creating a list of user data as Map<String, Object>
        List<Map<String, Object>> users = new ArrayList<>();

        // Create User 
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", 1L);
        user1.put("email", "user1@example.com");
        user1.put("role", role);
        
        // Add users to the list
        users.add(user1);

        return users;
    }


    /**
    * Creates a list of mock UserEntity objects.
    *
    * @return a List of UserEntity containing mock user data
    */
    public static  List<UserEntity> UserListRepository() {
        return UserMockData.UserList().stream().map(userMap -> {
            UserEntity user = new UserEntity();
            user.setId((Long) userMap.get("id"));
            user.setEmail((String) userMap.get("email"));
            user.setRole((RolesEntity) userMap.get("role"));
            return user;
        }).collect(Collectors.toList());
    }

    /**
    * Creates an UpdateUserDto object with mock data.
    *
    * @return an UpdateUserDto instance containing mock user update data
    */
    public static UpdateUserDto updateUserDto(){
        UpdateUserDto updateUserDto = new UpdateUserDto();

        updateUserDto.email="jhoeDoe@gmail.com";
        updateUserDto.password="SecretPassword";
        updateUserDto.role=1L;

        return updateUserDto;
    }

    /**
    * Creates a UserEntity object with mock data.
    *
    * @return a UserEntity instance containing mock user entity data
    */
    public static UserEntity updateUserEntity(){
        UserEntity entity = new UserEntity();

        entity.setId(1L);
        entity.setEmail("jhoeDoe@gmail.com");
        entity.setPassword("SecretPassword");
        entity.setRole(RolesMockData.rolesEntityList());

        return entity;
    }
}
