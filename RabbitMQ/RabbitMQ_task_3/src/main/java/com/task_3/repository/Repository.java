package com.task_3.repository;

import com.task_3.models.MyMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<MyMessage, Long> {
}
