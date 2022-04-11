package com.project.endpoints;

import com.project.entities.Book;
import com.project.entities.User;
import com.project.repository.BookRepo;
import com.project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainEndpoint {

    @Autowired
    UserRepo userRepo;

    @Autowired
    BookRepo bookRepo;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());

        if (user == null){
            return "login";
        }

        List<Book> books = bookRepo.findAll();
        model.addAttribute("role", user.getRole());
        model.addAttribute("books", books);
        return "index";
    }
}
