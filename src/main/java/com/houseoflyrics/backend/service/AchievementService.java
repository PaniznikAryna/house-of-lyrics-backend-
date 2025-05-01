package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Achievement;
import com.houseoflyrics.backend.entity.AchievementStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.AchievementRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementStatusService achievementStatusService;
    private final UserService userService;

    public AchievementService(AchievementRepository achievementRepository,
                              AchievementStatusService achievementStatusService,
                              UserService userService) {
        this.achievementRepository = achievementRepository;
        this.achievementStatusService = achievementStatusService;
        this.userService = userService;
    }

    public List<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public Optional<Achievement> findById(Long id) {
        return achievementRepository.findById(id);
    }

    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public boolean deleteAchievement(Long id) {
        if (achievementRepository.existsById(id)) {
            achievementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void assignDefaultStatusToAllUsers(Achievement achievement) {
        List<Users> allUsers = userService.findAll();
        for (Users user : allUsers) {
            AchievementStatus defaultStatus = new AchievementStatus(
                    user,
                    achievement,
                    false,
                    null
            );
            achievementStatusService.saveAchievementStatus(defaultStatus);
        }
    }
}
