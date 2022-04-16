package com.project.repository;

import com.project.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepo extends JpaRepository<Record, Long> {
    List<Record> findByUserId(long id);
}
