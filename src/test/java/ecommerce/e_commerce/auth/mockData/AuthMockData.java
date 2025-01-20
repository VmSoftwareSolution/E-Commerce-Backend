package ecommerce.e_commerce.auth.mockData;

import ecommerce.e_commerce.auth.dto.CreateUserDto;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.user.entity.UserEntity;

public class AuthMockData {
    


    /**
     * Create a DTO for creating a new user.
     * This DTO contains the basic information required to define a user,
     * including its email and password
     * 
     * @return a {@link CreateUserDto} instance pre-filled with default value
    */
    public static CreateUserDto createUserDto(){
        CreateUserDto dto = new CreateUserDto();

        dto.email="JhoeDoe@gmail.com";
        dto.password="mySecretPassword";

        return dto;
    }

    /**
     * Creates a UserEntity object from a CreateUserDto.
     * 
     * @param dto the {@Link CreateUserDto} containing the user details
     * @return a {@Link UserEntity} instance populated with the provided data
    */
    public static UserEntity userEntity(
        CreateUserDto createUserDto
    ){
        UserEntity entity = new UserEntity();
        entity.setEmail(createUserDto.email);
        entity.setPassword(createUserDto.password);

        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setName("Guest");
        rolesEntity.setDescription("user someone permission");
        
        entity.setRole(rolesEntity);

        return entity;
    }
}
