package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByTask_Id(Long taskId);
    List<AnswerOption> findByTask_IdAndCorrectTrue(Long taskId);
}
