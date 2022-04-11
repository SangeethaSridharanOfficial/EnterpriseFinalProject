package com.project.impl;

import com.project.dao.UserDao;
import com.project.dto.RegistrationDto;
import com.project.entities.User;
import com.project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        return null;
    }

    @Override
    public User signup(RegistrationDto registrationDto) {
        User user = new User(registrationDto.getName(), registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getAddress(), registrationDto.getPhone_no(), registrationDto.getRole());
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid Credentials");
        }
        ArrayList<String> roles = new ArrayList<>();
        roles.add("user");
        roles.add("admin");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(roles));

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }
}
