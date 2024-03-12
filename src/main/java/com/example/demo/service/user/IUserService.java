package com.example.demo.service.user;

import com.example.demo.table.User;
import com.example.demo.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    User findByUsername(String username);
    List<User> findALl();
}
