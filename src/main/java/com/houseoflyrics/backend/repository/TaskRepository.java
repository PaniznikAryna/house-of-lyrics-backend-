package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTest_Id(Long testId);
}
