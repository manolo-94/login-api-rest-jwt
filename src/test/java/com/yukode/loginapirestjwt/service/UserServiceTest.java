package com.yukode.loginapirestjwt.service;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.util.PasswordEncoderUtil;
import org.mockito.Mock;
import com.yukode.loginapirestjwt.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.MockitoAnnotations;

import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class UserServiceTest {
    
    @Mock // Inidica que sera un objeto simulado o un objeto @Mock
    private UserRepository userRepository;
    
    @Mock // Inidica que sera un objeto simulado o un objeto @Mock
    private PasswordEncoderUtil passwordEncoderUtil;
    
    @InjectMocks // Indica que esta clase recibira como dependecia los @Mock que hemos definido como parametros
    private UserService userService;
    
    private UserModel user1, user2;
    
    @BeforeEach // JUnit tiene un metodo que se ejecuta antes cada test
    void setUp(){
        
        MockitoAnnotations.openMocks(this); // Initializes @Mock and @InjectMock
        
        user1 = new UserModel();
        
        //Creamos un objeto de prueba 
        user1.setId_user(9L);
        user1.setName("test");
        user1.setLastname("test");
        user1.setEmail("test1@gmail.com");
        user1.setPhone("5555555555");
        user1.setPassword("123");
        user1.setRole("ROLE_USER");
        
        user2 = new UserModel();
        
        //Creamos un objeto de prueba 
        user2.setId_user(9L);
        user2.setName("test2");
        user2.setLastname("test2");
        user2.setEmail("test2@gmail.com");
        user2.setPhone("6666666666");
        user2.setPassword("123");
        user2.setRole("ROLE_USER");
        
    }
    
    @Test
    @DisplayName("Should register a new user successfully")
    void testRegisterUserSuccess(){
        
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoderUtil.encoderPassword(any(String.class))).thenReturn("passwordEncriptado");
        when(userRepository.save(any(UserModel.class))).thenReturn(user1);
        
        UserModel registeredUser = userService.register("test", "test", "5555555555", "test@gmail.com", "123", "ROLE_USER");       
        
        assertNotNull(registeredUser); // Esta afirmacion confirma que el objeto restiredUser no fue nulo y que fue guardado exitosamente.
        assertEquals("test@gmail.com", registeredUser.getEmail()); // Esta afirmacion confirma que el email es test@gmail.com y que fue configurado correctamente.
        
        verify(userRepository, times(1)).findUserByEmail("test@gmail.com"); // Esta line se asegura de que el metodo findUserByEmail fue llamado de UserResitory una vez con el email test@gmail.com. Esto asegura que el metodo fue usado como se esperaba en el proceso de registro.
        verify(userRepository, times(1)).save(any(UserModel.class)); // Esto verifica que que el metodo save fue llamado de userRepository una vez con un objeto userModel. Esto asegura que usaurio se ha guardao en el repositorio.
    }
    
    @Test
    @DisplayName("Should throw an exception when email is already registered")
    void testRegisterUserAlreadyExists(){
        
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(user1));
        
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            userService.register("test", "test", "5555555555", "test@gmail.com", "123", "ROLE_USER");       
        });
        
        assertEquals("User already exists whit this email", runtimeException.getMessage());
        verify(userRepository, times(1)).findUserByEmail("test@gmail.com");
        verify(userRepository, never()).save(any(UserModel.class));
    }
    
    @Test
    @DisplayName("Should return who has already been registered")
    void testFindUserByEmail(){
    
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(user1));
    
        Optional<UserModel> foundUser = userService.findByEmail("test@gmail.com");
        
        assertTrue(foundUser.isPresent());
        assertEquals("test@gmail.com", foundUser.get().getEmail());
        verify(userRepository, times(1)).findUserByEmail("test@gmail.com");
    }
    
    @Test
    @DisplayName("Test if getAllUser returns the correct list of users")
    void testListAllUsers(){
        
        List<UserModel> MockUsers = new ArrayList<>();
        
        MockUsers.add(user1);
        MockUsers.add(user2);
        
        when(userRepository.findAll()).thenReturn(MockUsers);
        
        List<UserModel> users = userService.getAllUsers();
        
        assertEquals(2, users.size());
        assertEquals("test1@gmail.com", users.get(0).getEmail());
        assertEquals("test2@gmail.com", users.get(1).getEmail());
        verify(userRepository, times(1)).findAll();
        
        
    }
    
    
}
