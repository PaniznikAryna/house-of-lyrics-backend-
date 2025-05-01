package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.AchievementStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.AchievementStatusRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementStatusService {
    private final AchievementStatusRepository achievementStatusRepository;
    private final UserService userService;

    public AchievementStatusService(AchievementStatusRepository achievementStatusRepository, UserService userService) {
        this.achievementStatusRepository = achievementStatusRepository;
        this.userService = userService;
    }

    public List<AchievementStatus> findAll() {
        return achievementStatusRepository.findAll();
    }

    public Optional<AchievementStatus> findById(Long id) {
        return achievementStatusRepository.findById(id);
    }

    public List<AchievementStatus> findByUserId(Long userId) {
        return achievementStatusRepository.findByUser_Id(userId);
    }

    public long countByUserAndStatus(Users user, boolean status) {
        return achievementStatusRepository.countByUserAndStatus(user, status);
    }

    public AchievementStatus saveAchievementStatus(AchievementStatus achievementStatus) {
        return achievementStatusRepository.save(achievementStatus);
    }

    public boolean deleteAchievementStatus(Long id) {
        if (achievementStatusRepository.existsById(id)) {
            achievementStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void assignDefaultAchievementStatusToUser(Users user) {
    }

}
