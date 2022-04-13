package com.project.repository;

import com.project.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM book b WHERE CONCAT(b.name, b.author, b.category, b.description,b.price) LIKE %:keyword%",nativeQuery = true)
    List<Book> findByKeyword(@Param("keyword") String keyword);
}
