package com.yukode.loginapirestjwt.exception;

public class UserInvalidCredentialException extends RuntimeException{
    
    public UserInvalidCredentialException(String message){
        super(message);
    }
    
}
