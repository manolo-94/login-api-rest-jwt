package com.yukode.loginapirestjwt.mapper;

import com.yukode.loginapirestjwt.dto.UserDTO;
import com.yukode.loginapirestjwt.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDTO toUserDto(UserModel user){
        
        UserDTO dto = new UserDTO();
        
        dto.setId_user(user.getId_user());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        
        return dto;
    }
    
}
