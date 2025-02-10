package ecommerce.e_commerce.user;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import ecommerce.e_commerce.auth.mockData.AuthMockData;
import ecommerce.e_commerce.common.interfaces.user.UserServiceInterface;
import ecommerce.e_commerce.user.mockData.UserMockData;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Value("${app.api-prefix}")
    private String apiPrefix;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceInterface userServiceInterface;

    @Test
    public void testFindUsers() throws Exception{

        Mockito.when(userServiceInterface.findUser(any())).thenReturn(UserMockData.UserList());

        String token = AuthMockData.generateTokenAdmin();
        
        mockMvc.perform(MockMvcRequestBuilders.get(apiPrefix+"/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers
            .status()
            .isOk()); 
    }

    @Test
    public void testUpdateUser() throws Exception{
        Long id = 1L;
        String json = objectMapper.writeValueAsString(UserMockData.updateUserDto());

        Mockito.when(userServiceInterface.updateUser(id, UserMockData.updateUserDto()))
            .thenReturn(UserMockData.updateUserEntity());

        String token = AuthMockData.generateTokenAdmin();
        
        mockMvc.perform(MockMvcRequestBuilders.put(apiPrefix+"/users/"+id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .content(json))
            .andExpect(MockMvcResultMatchers
            .status()
            .isOk()); 
    }


    @Test
    public void testFindUsersDetail() throws Exception{

        Long id = 1L;
        Mockito.when(userServiceInterface.findUserDetail(id))
            .thenReturn(UserMockData.UserListDetail());

        String token = AuthMockData.generateTokenAdmin();
        
        mockMvc.perform(MockMvcRequestBuilders.get(apiPrefix+"/users/"+id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers
            .status()
            .isOk()); 
    }

}
