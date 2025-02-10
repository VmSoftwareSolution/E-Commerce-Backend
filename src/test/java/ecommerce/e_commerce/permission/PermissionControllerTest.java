package ecommerce.e_commerce.permission;

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
import ecommerce.e_commerce.common.interfaces.permission.PermissionServiceInterface;
import ecommerce.e_commerce.permission.entity.PermissionEntity;
import ecommerce.e_commerce.permission.mockData.PermissionMockData;

@SpringBootTest
@AutoConfigureMockMvc
public class PermissionControllerTest {
 
    @Value("${app.api-prefix}")
    private String apiPrefix;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PermissionServiceInterface permissionServiceInterface;


    @Test
    public void testSavePermissionSuccessfully() throws Exception{
        String json = objectMapper.writeValueAsString(PermissionMockData.createPermissionDto());

        Mockito.when(permissionServiceInterface.createPermission(any())).thenReturn(new PermissionEntity());

        String token = AuthMockData.generateTokenAdmin(); 

        mockMvc.perform(MockMvcRequestBuilders.post(apiPrefix+"/permission")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers
            .status()
            .isCreated()); 
    }

    @Test
    public void testFindPermissionSuccessfully() throws Exception{
        
        Mockito.when(permissionServiceInterface.findPermission(any()))
            .thenReturn(PermissionMockData.permissionEntityList());
        
        String token = AuthMockData.generateTokenAdmin();
        
        mockMvc.perform(MockMvcRequestBuilders.get(apiPrefix+"/permission")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers
                .status()
                .isOk()
            ); 
    }

    @Test
    public void testUpdatePermissionSuccessfully() throws Exception{
        Long id = 1L;
        String json = objectMapper.writeValueAsString(PermissionMockData.updatePermissionDto());

        Mockito.when(permissionServiceInterface.updatePermission(
            id, PermissionMockData.updatePermissionDto())
        ).thenReturn(PermissionMockData.updatePermissionEntity());

        String token = AuthMockData.generateTokenAdmin(); 

        mockMvc.perform(MockMvcRequestBuilders.put(apiPrefix+"/permission/"+id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json).header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers
            .status()
            .isOk()); 
    }

}
