
package com.project.endpoints;

import com.project.entities.Book;
import com.project.entities.User;
import com.project.repository.BookRepo;
import com.project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainEndpoint {

    @Autowired
    UserRepo userRepo;

    @Autowired
    BookRepo bookRepo;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if(request.getQueryString() != null && request.getQueryString().equals("logout")){
            try {
                request.logout();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        return "login";
    }

    @GetMapping(path = {"/", "/search"})
    public String home(Model model,
                       String keyword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());

        if (auth == null || auth instanceof AnonymousAuthenticationToken || user == null) {
            return "redirect:/";
        }
        List<Book> books;
        if (keyword != null) {
            books = bookRepo.findByKeyword(keyword);
            model.addAttribute("role", user.getRole());
            model.addAttribute("books", books);
        }
        else {
            books = bookRepo.findAll();
            model.addAttribute("role", user.getRole());
            findPaginated(1,"name", "asc", model);
        }

        return "index";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDirection") String sortDirection,
                                Model model) {

        System.out.println(" " + pageNo+" " + sortField+" " +sortDirection);
        List<Book> books = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        model.addAttribute("role", user.getRole());
        if(sortField.equals("no_of_copies")){
            books = bookRepo.findAll();
            model.addAttribute("books", books);
            return "index";
        }
        int pageSize = 5;
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name().toString()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        System.out.println(sort);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Book> page = bookRepo.findAll(pageable);
        books = page.getContent();
        System.out.println(books);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("books", books);
        return "index";
    }

}
