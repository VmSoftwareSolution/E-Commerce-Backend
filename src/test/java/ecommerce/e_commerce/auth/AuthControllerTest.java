package ecommerce.e_commerce.auth;

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
import ecommerce.e_commerce.common.interfaces.auth.AuthServiceInterface;
import ecommerce.e_commerce.user.entity.UserEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    
    @Value("${app.api-prefix}")
    private String apiPrefix;
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthServiceInterface authServiceInterface;

    @Test
    public void testSaveUserSuccessfully() throws Exception{
        String json = objectMapper.writeValueAsString(AuthMockData.createUserDto());

        Mockito.when(
            authServiceInterface.createUser(any())
        ).thenReturn(new UserEntity());

        mockMvc.perform(MockMvcRequestBuilders.post(apiPrefix+"/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers
            .status()
            .isCreated()); 
    }
}
