package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.TestComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestCompositionRepository extends JpaRepository<TestComposition, Long> {
    List<TestComposition> findByComposition_Id(Long compositionId);
}
