package com.task_2.repository;

import com.task_2.models.MyMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<MyMessage, Long> {
}
