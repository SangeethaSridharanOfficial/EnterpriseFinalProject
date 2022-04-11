package com.project.dao;

import com.project.dto.RegistrationDto;
import com.project.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserDao extends UserDetailsService {
    String login(String email, String password);
    User signup(RegistrationDto registrationDto);
}
