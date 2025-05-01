package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.AchievementStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.AchievementStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/achievementStatus")
public class AchievementStatusController {

    private final AchievementStatusService achievementStatusService;
    private final UserService userService;

    public AchievementStatusController(AchievementStatusService achievementStatusService, UserService userService) {
        this.achievementStatusService = achievementStatusService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AchievementStatus>> getAllAchievementStatus(
            @RequestHeader("Authorization") String token) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isPresent() && currentUser.get().isAdmin()) {
            List<AchievementStatus> statuses = achievementStatusService.findAll();
            return ResponseEntity.ok(statuses);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementStatus> getAchievementStatusById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (!currentUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<AchievementStatus> optStatus = achievementStatusService.findById(id);
        if (!optStatus.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        AchievementStatus status = optStatus.get();
        if (currentUser.get().isAdmin() || currentUser.get().getId().equals(status.getUser().getId())) {
            return ResponseEntity.ok(status);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<AchievementStatus>> getMyAchievementStatus(
            @RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        List<AchievementStatus> achievementStatuses = achievementStatusService.findByUserId(currentUser.getId());
        return ResponseEntity.ok(achievementStatuses);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAchievedCountForUser(
            @RequestHeader("Authorization") String token) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (!currentUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = achievementStatusService.countByUserAndStatus(currentUser.get(), true);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/add")
    public ResponseEntity<AchievementStatus> addAchievementStatus(
            @RequestHeader("Authorization") String token,
            @RequestBody AchievementStatus achievementStatus) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (!currentUser.isPresent() || !currentUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AchievementStatus savedStatus = achievementStatusService.saveAchievementStatus(achievementStatus);
        return ResponseEntity.ok(savedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAchievementStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (!currentUser.isPresent() || !currentUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        boolean deleted = achievementStatusService.deleteAchievementStatus(id);
        if (deleted) {
            return ResponseEntity.ok("Достижение удалено");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
