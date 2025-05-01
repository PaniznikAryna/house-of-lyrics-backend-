package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Dictation;
import com.houseoflyrics.backend.entity.DictationStatus;
import com.houseoflyrics.backend.entity.Statistics;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.AchievementStatusService;
import com.houseoflyrics.backend.service.DictationService;
import com.houseoflyrics.backend.service.DictationStatusService;
import com.houseoflyrics.backend.service.StatisticsService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final StatisticsService statisticsService;
    private final DictationService dictationService;
    private final DictationStatusService dictationStatusService;
    private final AchievementStatusService achievementStatusService;

    public AuthController(UserService userService,
                          StatisticsService statisticsService,
                          DictationService dictationService,
                          DictationStatusService dictationStatusService,
                          AchievementStatusService achievementStatusService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
        this.dictationService = dictationService;
        this.dictationStatusService = dictationStatusService;
        this.achievementStatusService = achievementStatusService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        if (userService.findByMail(user.getMail()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь с данной почтой уже существует");
        }
        user.setAdmin(false);
        Users registeredUser = userService.registerUser(user);

        Statistics newStats = new Statistics(registeredUser, 0, 0, 0, 0);
        statisticsService.saveStatistics(newStats);

        List<Dictation> allDictations = dictationService.findAll();
        for (Dictation dictation : allDictations) {
            DictationStatus ds = new DictationStatus(
                    dictation,
                    registeredUser,
                    DictationStatus.DictationStatusEnum.НЕ_НАЧАТО,
                    0.0
            );
            dictationStatusService.saveDictationStatus(ds);
        }

        achievementStatusService.assignDefaultAchievementStatusToUser(registeredUser);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String mail, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticate(mail, password);
        if (isAuthenticated) {
            String token = JwtUtil.generateToken(mail);
            return ResponseEntity.ok("Токен: " + token);
        } else {
            return ResponseEntity.status(401).body("Ошибка авторизации");
        }
    }
}
