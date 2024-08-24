package com.yukode.loginapirestjwt.controller;

import com.yukode.loginapirestjwt.dto.UserLoginDTO;
import com.yukode.loginapirestjwt.dto.UserDTO;
import com.yukode.loginapirestjwt.mapper.UserMapper;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.security.JwtUtils;
import com.yukode.loginapirestjwt.service.UserService;
import com.yukode.loginapirestjwt.util.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserLoginDTO loginRequestDTO) {

            UserModel user = userService.register(
                    loginRequestDTO.getName(),
                    loginRequestDTO.getLastname(),
                    loginRequestDTO.getPhone(),
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword(),
                    loginRequestDTO.getRole()
            );

            return ResponseEntity.ok(userMapper.toUserDto(user));

    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {

        UserModel user = userService.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not faund"));

        if (passwordEncoderUtil.matches(userLoginDTO.getPassword(), user.getPassword())) {

            String token = jwtUtils.generateJwtToken(user.getEmail(), user.getRole());

            return ResponseEntity.ok(token);

        } else {

            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }

}
