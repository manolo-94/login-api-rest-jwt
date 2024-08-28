package com.yukode.loginapirestjwt.service;

import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.util.PasswordEncoderUtil;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yukode.loginapirestjwt.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRespository;
    
    @Autowired
    private  PasswordEncoderUtil passwordEncoderUtil;
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public UserModel register(String name, String lastname, String phone, String email, String password, String role){
        
        logger.info("Registering user with email: {}", email);
        
        Optional<UserModel> existingUser = userRespository.findUserByEmail(email);
        
        if(existingUser.isPresent()){
            
            logger.warn("User already exists with email: {}", email);
            
            throw new RuntimeException("User already exists whit this email");
        }
        
        UserModel newUser = new UserModel();
        
        newUser.setName(name);
        newUser.setLastname(lastname);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoderUtil.encoderPassword(password));
        newUser.setRole(role != null ? role : "ROLE_USER"); // Assign role or default to ROLE_USER
        
        logger.info("User registered successfully with email: {}", email);
        
        return userRespository.save(newUser);
        
    }
    
    public Optional<UserModel> findByEmail(String email){
        
        return userRespository.findUserByEmail(email);
        
    }
    
    public List<UserModel> getAllUsers(){
        
        return userRespository.findAll();
        
    }
    
}
