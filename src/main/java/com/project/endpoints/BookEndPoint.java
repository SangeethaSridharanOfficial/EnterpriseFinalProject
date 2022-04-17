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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/books")
public class BookEndPoint {


    @Autowired
    UserRepo userRepo;
    @Autowired
    BookRepo bookRepo;

    @GetMapping("/newBook")
    public String showAddBook(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        model.addAttribute("role", user.getRole());
        Book book = new Book();
        model.addAttribute("book", book);
        return "newBook";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        model.addAttribute("role", user.getRole());
        bookRepo.save(book);
        return "redirect:/";
    }

    //---------------Method to Update the Book------------
    @GetMapping(value ="/edit/{id}")
    public ModelAndView updateBook(@PathVariable(name = "id") long id, Model model) {
        ModelAndView mav = new ModelAndView("editBook");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        model.addAttribute("role", user.getRole());
        Book book = bookRepo.findById(id).get();
        mav.addObject("book", book);
        return mav;
    }


    //------------------Method to Delete the Book--------------
    @GetMapping("/delete")
    public String deleteBook(@RequestParam(name="id", required=true) long id) {
        bookRepo.deleteById(id);
        return "redirect:/";
    }
}
