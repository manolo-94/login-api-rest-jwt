package com.yukode.loginapirestjwt.controller;

import com.yukode.loginapirestjwt.dto.UserLoginDTO;
import com.yukode.loginapirestjwt.dto.UserDTO;
import com.yukode.loginapirestjwt.mapper.UserMapper;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.security.JwtUtils;
import com.yukode.loginapirestjwt.service.UserService;
import com.yukode.loginapirestjwt.util.PasswordEncoderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Register", description = "Endpoint to register a new user ")
     @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful new user created",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserModel.class))),
        @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    public ResponseEntity<UserDTO> register(@Parameter( description = "UserModel to be created", required = true) @RequestBody UserLoginDTO loginRequestDTO) {

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
    @Operation(summary = "Login", description = "Endpoint to login user ")
     @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful return TOKEN",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserModel.class))),
        @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
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
