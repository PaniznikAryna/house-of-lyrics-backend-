package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.TestCompositionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestCompositionStatusRepository extends JpaRepository<TestCompositionStatus, Long> {
    List<TestCompositionStatus> findByUser_Id(Long userId);
    List<TestCompositionStatus> findByResultLessThan(Double value);
    List<TestCompositionStatus> findByResultGreaterThanEqual(Double value);
    Optional<TestCompositionStatus> findByTestComposition_IdAndUser_Id(Long testCompositionId, Long userId);
}
