package com.yukode.loginapirestjwt.controller;

import com.yukode.loginapirestjwt.dto.UserDTO;
import com.yukode.loginapirestjwt.dto.UserLoginDTO;
import com.yukode.loginapirestjwt.mapper.UserMapper;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.security.JwtUtils;
import com.yukode.loginapirestjwt.service.UserService;
import com.yukode.loginapirestjwt.util.PasswordEncoderUtil;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.ResponseEntity;

public class AuthControllerTest {
    
    @Mock
    private UserService userService;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private PasswordEncoderUtil passwordEncoderUtil;
    
    @Mock
    private JwtUtils jwtUtils;
    
    @InjectMocks
    private AuthController authController;
    
    private UserModel user1, user2;
    private UserDTO userDTO;
    private UserLoginDTO userLoginDTO;
    
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        
        user1 = new UserModel();
        
        user1.setId_user(8L);
        user1.setName("test1");
        user1.setLastname("testLastName1");
        user1.setEmail("test1@gmail.com");
        user1.setPhone("111111111");
        user1.setPassword("encodedPassword");
        user1.setRole("ROLE_USER");
        
        user2 = new UserModel();
        
        user2.setId_user(8L);
        user2.setName("test2");
        user2.setLastname("testLastName2");
        user2.setEmail("test2@gmail.com");
        user2.setPhone("2222222222");
        user2.setPassword("encodedPassword");
        user2.setRole("ROLE_USER");
        
        userDTO = new UserDTO();
        
        userDTO.setId_user(8L);
        userDTO.setName("test1");
        userDTO.setLastname("testLastName1");
        userDTO.setEmail("test1@gmail.com");
        userDTO.setPhone("111111111");
        userDTO.setRole("ROLE_USER");
        
        userLoginDTO = new UserLoginDTO();
        
        userLoginDTO.setName("test1");
        userLoginDTO.setLastname("testLastName1");
        userLoginDTO.setEmail("test1@gmail.com");
        userLoginDTO.setPhone("111111111");
        userLoginDTO.setPassword("password123");
        userLoginDTO.setRole("ROLE_USER");
        
    }
    
    @Test
    @DisplayName("Test successfull user registration")
    void testRegisterSuccess(){
        
        when(userService.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(user1);
        when(userMapper.toUserDto(any(UserModel.class))).thenReturn(userDTO);
        
        ResponseEntity<UserDTO> responseEntity = authController.register(userLoginDTO);
        
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("test1@gmail.com", responseEntity.getBody().getEmail());
        
        verify(userService, times(1)).register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(userMapper, times(1)).toUserDto(user1);
    }
    
    @Test
    @DisplayName("Test successfull login")
    void testLoginSuccess(){
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(passwordEncoderUtil.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateJwtToken(anyString(), anyString())).thenReturn("mockedToken");
        
        ResponseEntity<String> responseEntity = authController.login(userLoginDTO);
        
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("mockedToken", responseEntity.getBody());
        
        verify(userService, times(1)).findByEmail("test1@gmail.com");
        verify(passwordEncoderUtil, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtils, times(1)).generateJwtToken("test1@gmail.com", "ROLE_USER");
        
    }
    
    @Test
    @DisplayName("Test login invalid credentials")
    void testLoginInvalidCredentials(){
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(passwordEncoderUtil.matches(anyString(), anyString())).thenReturn(false);
        
        ResponseEntity<String> response = authController.login(userLoginDTO);
        
        assertNotNull(response);
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid Credentials", response.getBody());
        
        verify(userService, times(1)).findByEmail("test1@gmail.com");
        verify(passwordEncoderUtil, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtils, times(0)).generateJwtToken(anyString(), anyString());
    }
}
