package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
}
