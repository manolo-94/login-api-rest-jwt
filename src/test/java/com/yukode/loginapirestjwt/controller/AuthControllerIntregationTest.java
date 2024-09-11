package com.yukode.loginapirestjwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukode.loginapirestjwt.dto.UserLoginDTO;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.repository.UserRepository;
import com.yukode.loginapirestjwt.util.PasswordEncoderUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Loads the full application context
@AutoConfigureMockMvc
public class AuthControllerIntregationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    @BeforeEach
    void setUp() {
        
        userRepository.deleteAll();

    }

    @Test
    void testRegisterSuccess() throws Exception {

        UserLoginDTO newUser = builderUserDTO();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser)))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {

        UserModel userModel = new UserModel();

        userModel.setName("test");
        userModel.setLastname("testL");
        userModel.setEmail("test@gmail.com");
        userModel.setPhone("999999999");
        userModel.setPassword(passwordEncoderUtil.encoderPassword("123"));
        userModel.setRole("ROLE_USER");
        userRepository.save(userModel);

        UserLoginDTO userLoginDTO = builderUserDTO();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(userLoginDTO))) // Convert to JSON and send JSON format
                .andExpect(status().isConflict())
                .andReturn();

        assertNotNull(result);
        assertEquals(409, result.getResponse().getStatus());

    }

    @Test
    void testLoginInvalidCredential() throws Exception {

        UserLoginDTO userLoginDTO = builderUserDTO();

        MvcResult result = (MvcResult) mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(userLoginDTO))) // Convert to JSON and send JSON format
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertNotNull(result);
        assertEquals(401, result.getResponse().getStatus());

    }

    private String mapToJson(Object object) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(object);
    }

    private UserLoginDTO builderUserDTO() {

        UserLoginDTO userLoginDTO = new UserLoginDTO();

        userLoginDTO.setName("test");
        userLoginDTO.setLastname("testL");
        userLoginDTO.setEmail("test@gmail.com");
        userLoginDTO.setPhone("999999999");
        userLoginDTO.setPassword("123");
        userLoginDTO.setRole("ROLE_USER");

        return userLoginDTO;
    }
}
