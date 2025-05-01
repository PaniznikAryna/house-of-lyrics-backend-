package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Achievement;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.AchievementService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/achievement")
public class AchievementController {

    private final AchievementService achievementService;
    private final UserService userService;

    public AchievementController(AchievementService achievementService, UserService userService) {
        this.achievementService = achievementService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Achievement>> getAllAchievement(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<Achievement> achievements = achievementService.findAll();
            return ResponseEntity.ok(achievements);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(@RequestHeader("Authorization") String token,
                                                          @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Achievement> achievement = achievementService.findById(id);
            return achievement.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAchievement(@RequestHeader("Authorization") String token,
                                            @RequestBody Achievement achievement) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Achievement savedAchievement = achievementService.saveAchievement(achievement);
            achievementService.assignDefaultStatusToAllUsers(savedAchievement);
            return ResponseEntity.ok(savedAchievement);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAchievement(@RequestHeader("Authorization") String token,
                                               @PathVariable Long id,
                                               @RequestBody Achievement achievement) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Achievement> existingAchievementOpt = achievementService.findById(id);
            if (existingAchievementOpt.isPresent()) {
                Achievement existingAchievement = existingAchievementOpt.get();
                existingAchievement.setTitle(achievement.getTitle());
                existingAchievement.setPicture(achievement.getPicture());
                existingAchievement.setConditionsOfReceipt(achievement.getConditionsOfReceipt());
                Achievement savedAchievement = achievementService.saveAchievement(existingAchievement);
                return ResponseEntity.ok(savedAchievement);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAchievement(@RequestHeader("Authorization") String token,
                                                    @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = achievementService.deleteAchievement(id);
            return deleted ? ResponseEntity.ok("Достижение удалено") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
