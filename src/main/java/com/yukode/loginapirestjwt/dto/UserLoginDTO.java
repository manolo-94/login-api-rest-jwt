package com.yukode.loginapirestjwt.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    
}
