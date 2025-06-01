package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.dto.AuthResponseDTO;
import com.houseoflyrics.backend.entity.*;
import com.houseoflyrics.backend.service.*;
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
    private final TestService testService;
    private final TestStatusService testStatusService;
    private final AchievementStatusService achievementStatusService;

    private final TestCompositionService testCompositionService;
    private final TestCompositionStatusService testCompositionStatusService;

    public AuthController(UserService userService,
                          StatisticsService statisticsService,
                          DictationService dictationService,
                          DictationStatusService dictationStatusService,
                          TestService testService,
                          TestStatusService testStatusService,
                          AchievementStatusService achievementStatusService,
                          TestCompositionService testCompositionService,
                          TestCompositionStatusService testCompositionStatusService) {

        this.userService = userService;
        this.statisticsService = statisticsService;
        this.dictationService = dictationService;
        this.dictationStatusService = dictationStatusService;
        this.testService = testService;
        this.testStatusService = testStatusService;
        this.achievementStatusService = achievementStatusService;
        this.testCompositionService = testCompositionService;
        this.testCompositionStatusService = testCompositionStatusService;
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

        List<TestComposition> allCompositionTests = testCompositionService.findAll();
        for (TestComposition test : allCompositionTests) {
            TestCompositionStatus ts = new TestCompositionStatus(test, registeredUser, 0.0);
            testCompositionStatusService.saveStatus(ts);
        }


        List<Test> allTests = testService.findAll();
        for (Test test : allTests) {
            TestStatus ts = new TestStatus(
                    test,
                    registeredUser,
                    TestStatus.TestStatusEnum.НЕ_НАЧАТО,
                    0.0
            );
            testStatusService.saveTestStatus(ts);
        }

        achievementStatusService.assignDefaultAchievementStatusToUser(registeredUser);


        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String mail, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticate(mail, password);
        if (isAuthenticated) {
            String token = JwtUtil.generateToken(mail);
            Users user = userService.findByMail(mail).orElse(null);
            if (user == null) {
                return ResponseEntity.status(404).body("Пользователь не найден");
            }
            AuthResponseDTO response = new AuthResponseDTO(token, user.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Ошибка авторизации");
        }
    }

}
