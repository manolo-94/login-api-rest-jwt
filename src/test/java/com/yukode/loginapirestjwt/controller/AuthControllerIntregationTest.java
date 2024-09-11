package com.yukode.loginapirestjwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukode.loginapirestjwt.dto.UserDTO;
import com.yukode.loginapirestjwt.dto.UserLoginDTO;
import com.yukode.loginapirestjwt.exception.UserAlreadyExistsException;
import com.yukode.loginapirestjwt.exception.UserInvalidCredentialException;
import com.yukode.loginapirestjwt.mapper.UserLoginMapper;
import com.yukode.loginapirestjwt.mapper.UserMapper;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.repository.UserRepository;
import com.yukode.loginapirestjwt.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Loads the full application context
//@WebAppConfiguration // Configures a WebApplicationContext
@AutoConfigureMockMvc
public class AuthControllerIntregationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

//        userRepository.deleteAll();

//        UserModel userModel = new UserModel();
//        
//        UserDTO userDTO = new UserDTO();
//        
//        userModel.setId_user(15L);
//        userModel.setName("test");
//        userModel.setLastname("testL");
//        userModel.setEmail("test@gmail.com");
//        userModel.setPhone("999999999");
//        userModel.setPassword("myPasswordEncoded");
//        userModel.setRole("ROLE_USER");
//        
//        userDTO.setName("test");
//        userDTO.setLastname("testL");
//        userDTO.setEmail("test@gmail.com");
//        userDTO.setPhone("999999999");
//        userDTO.setRole("ROLE_USER");
//        
//        when(userService.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(userModel);
//        when(userMapper.toUserDto(any(UserModel.class))).thenReturn(userDTO);
    }

    @Test
    void testRegisterSuccess() throws Exception {

        UserLoginDTO newUser = builderUserDTO();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser)))
                .andReturn();

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {

//        UserModel userModel = new UserModel();
//
//        userModel.setName("test");
//        userModel.setLastname("testL");
//        userModel.setEmail("test@gmail.com");
//        userModel.setPhone("999999999");
//        userModel.setPassword("myPasswordEncoded");
//        userModel.setRole("ROLE_USER");
//        
//        userRepository.save(userModel);

        UserLoginDTO userLoginDTO = builderUserDTO();

        when(userService.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new UserAlreadyExistsException("User already exists whit this email"));

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

        when(userService.login(anyString(), anyString()))
                .thenThrow(new UserInvalidCredentialException("Invalid user or email"));

        MvcResult result = (MvcResult) mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(userLoginDTO))) // Convert to JSON and send JSON format
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
        userLoginDTO.setPassword("myPassword123");
        userLoginDTO.setRole("ROLE_USER");

        return userLoginDTO;
    }
}
