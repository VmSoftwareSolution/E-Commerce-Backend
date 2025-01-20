package ecommerce.e_commerce.roles;

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

import ecommerce.e_commerce.common.interfaces.roles.RolesServiceInterface;
import ecommerce.e_commerce.roles.entity.RolesEntity;
import ecommerce.e_commerce.roles.mockData.RolesMockData;

@SpringBootTest
@AutoConfigureMockMvc
public class RolesControllerTest {
    
    @Value("${app.api-prefix}")
    private String apiPrefix;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RolesServiceInterface rolesServiceInterface;


    @Test
    public void testSaveRolesSuccessfully()throws Exception{
        String json = objectMapper.writeValueAsString(RolesMockData.createRolesDto());
    
        Mockito.when(rolesServiceInterface.createRoles(any())).thenReturn(new RolesEntity());
     
        mockMvc.perform(MockMvcRequestBuilders.post(apiPrefix+"/roles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers
            .status()
            .isCreated()); 
    }
}
