package com.yukode.loginapirestjwt.mapper;

import com.yukode.loginapirestjwt.dto.UserLoginDTO;
import com.yukode.loginapirestjwt.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserLoginMapper {
    
    public UserLoginDTO toUserLoginDto(UserModel user){
        
        UserLoginDTO dto = new UserLoginDTO();
       
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        
        return dto;
        
    }

    
}
