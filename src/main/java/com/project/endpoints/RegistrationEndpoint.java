package com.project.endpoints;

import com.project.dao.UserDao;
import com.project.dto.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationEndpoint {

    @Autowired
    private UserDao userDao;

    @ModelAttribute("user")
    public RegistrationDto registrationDto(){
        return new RegistrationDto();
    }

    @GetMapping
    public String showRegistration(){
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") RegistrationDto registrationDto){
        userDao.signup(registrationDto);
        return "redirect:/registration?success";
    }


}
