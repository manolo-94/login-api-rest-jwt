package com.yukode.loginapirestjwt.dto;

import lombok.Data;

@Data
public class UserDTO {
    
    private Long id_user;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String role;
    
}
