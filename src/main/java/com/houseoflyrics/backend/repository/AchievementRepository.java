package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}
