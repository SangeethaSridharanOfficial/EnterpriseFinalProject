package com.project.endpoints;

import com.project.entities.Book;
import com.project.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookEndPoint {

    @Autowired
    BookRepo bookRepo;

    @GetMapping("/newBook")
    public String showAddBook(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "newBook";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book){
        bookRepo.save(book);
        return "redirect:/";
    }
}
