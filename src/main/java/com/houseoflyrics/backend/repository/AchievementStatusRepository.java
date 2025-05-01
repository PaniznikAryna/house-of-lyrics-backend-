package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.AchievementStatus;
import com.houseoflyrics.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AchievementStatusRepository extends JpaRepository<AchievementStatus, Long> {
    List<AchievementStatus> findByUser_Id(Long userId);
    long countByUserAndStatus(Users user, boolean status);
}
