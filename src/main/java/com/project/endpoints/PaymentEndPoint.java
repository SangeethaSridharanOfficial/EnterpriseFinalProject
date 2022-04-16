package com.project.endpoints;

import com.project.entities.Book;
import com.project.entities.Payment;
import com.project.entities.Record;
import com.project.entities.User;
import com.project.repository.BookRepo;
import com.project.repository.PaymentRepo;
import com.project.repository.RecordRepo;
import com.project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequestMapping(path = {"/payments", "/page/payments"})
public class PaymentEndPoint {

    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    RecordRepo recordRepo;

    @Autowired
    BookRepo bookRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/buy")
    public String buyBook(@RequestParam(name="id", required = true) long id, Model model){
        Book book = bookRepo.findById(id).get();
        Payment payment = new Payment();
        model.addAttribute("payment", payment);
        model.addAttribute("id", id);
        model.addAttribute("book", book);
        return "payment";
    }

    @PostMapping(value = "/pay", params = "pay")
    public String payBook(@ModelAttribute("payment") Payment payment, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        Book book = bookRepo.findById(payment.getBookId()).get();


        int count = bookRepo.updateNoOfCopies(payment.getBookId(), book.getNo_of_copies() - 1);
        System.out.println("count " + count);

        payment.setUserId(user.getId());
        Date date = new Date();
        payment.setTransaction_date(date);

        paymentRepo.save(payment);

        Record record = new Record();
        record.setBorrowed(date);
        LocalDate localDate = LocalDate.now().plusDays(7);
        record.setDue(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        record.setBookId(book.getId());
        record.setUserId(payment.getUserId());
        record.setReturned(null);

        recordRepo.save(record);
        return "redirect:/";
    }

    @PostMapping(value = "/pay", params = "cancel")
    public String cancelPayment(){
        return "redirect:/";
    }
}
