package com.yukode.loginapirestjwt.controller;

import com.yukode.loginapirestjwt.dto.UserDTO;
import com.yukode.loginapirestjwt.mapper.UserMapper;
import com.yukode.loginapirestjwt.model.UserModel;
import com.yukode.loginapirestjwt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "List of users", description = "Endpoint to list all users ")
     @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retuen Json",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserModel.class))),
        @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toUserDto)
                .toList();

        return ResponseEntity.ok(users);
    }

}
