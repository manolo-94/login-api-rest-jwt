package com.yukode.loginapirestjwt.repository;

import com.yukode.loginapirestjwt.model.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<UserModel, Long> {
    
    Optional<UserModel> findUserByEmail(String Email);
    
}
