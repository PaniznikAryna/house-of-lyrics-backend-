package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByLessonId(Long lessonId);
}
