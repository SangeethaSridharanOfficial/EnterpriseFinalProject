package com.project.endpoints;

import com.project.entities.Book;
import com.project.entities.Record;
import com.project.entities.User;
import com.project.repository.BookRepo;
import com.project.repository.RecordRepo;
import com.project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(path = {"/records"})
public class RecordEndPoint {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RecordRepo recordRepo;

    @Autowired
    BookRepo bookRepo;

    @GetMapping("/summary/user")
    public String showUserSummary(Model model){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepo.findByEmail(auth.getName());
            model.addAttribute("role", user.getRole());
            List<Record> recordList = recordRepo.findByUserId(user.getId());
            List<HashMap<String, Object>> result = new ArrayList<>();
            for(Record record: recordList){
                Book book = bookRepo.findById(record.getBookId()).get();
                HashMap<String, Object> summary = new HashMap<>();
                summary.put("id", record.getId());
                summary.put("name", book.getName());
                summary.put("borrowed", record.getBorrowed());
                summary.put("returned", record.getReturned());
                summary.put("due", record.getDue());
                result.add(summary);
            }

            model.addAttribute("record", result);
        }catch (Exception e){
            System.out.println("Error " + e);
        }
        return "userRecord";
    }

    @GetMapping("/summary/admin")
    public String showAdminSummary(Model model){

        List<Record> recordList = recordRepo.findAll();
        List<HashMap<String, Object>> result = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userObj = userRepo.findByEmail(auth.getName());
        model.addAttribute("role", userObj.getRole());

        for(Record record: recordList){
            Book book = bookRepo.findById(record.getBookId()).get();
            User user = userRepo.findById(record.getUserId()).get();
            HashMap<String, Object> summary = new HashMap<>();
            summary.put("id", record.getId());
            summary.put("userName", user.getName());
            summary.put("bookId", book.getId());
            summary.put("name", book.getName());
            summary.put("borrowed", record.getBorrowed());
            summary.put("returned", record.getReturned());
            summary.put("due", record.getDue());
            result.add(summary);
        }

        model.addAttribute("record", result);

        return "adminRecord";
    }

    @GetMapping("/return")
    public String returnBook(@RequestParam(name = "id", required = true) long id, Model model){
        Record record = recordRepo.findById(id).get();
        Book book = bookRepo.findById(record.getBookId()).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        model.addAttribute("role", user.getRole());
        if(book != null){
            bookRepo.updateNoOfCopies(book.getId(),book.getNo_of_copies() + 1);
        }
        recordRepo.deleteById(id);
        return "redirect:/records/summary/admin";
    }

    @GetMapping("/back")
    public String backToMainPage(){
        return "redirect:/";
    }

}
