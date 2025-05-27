package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByUser_Id(Long userId);
    List<Mark> findByComposition_Id(Long compositionId);
}
