package com.yukode.loginapirestjwt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderUtil {
    
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public String encoderPassword(String password){
    
        return encoder.encode(password);
        
    }
    
    public Boolean matches(String rawPassword, String encodePassword){
     
        return encoder.matches(rawPassword, encodePassword );
        
    }
    
}
