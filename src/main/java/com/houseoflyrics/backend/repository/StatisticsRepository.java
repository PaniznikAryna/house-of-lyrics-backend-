package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Optional<Statistics> findByUserId(Long userId);
}
