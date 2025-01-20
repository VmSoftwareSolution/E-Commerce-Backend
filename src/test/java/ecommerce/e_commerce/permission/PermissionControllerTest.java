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

        mockMvc.perform(MockMvcRequestBuilders.post(apiPrefix+"/permission")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers
            .status()
            .isCreated()); 
    }

}
