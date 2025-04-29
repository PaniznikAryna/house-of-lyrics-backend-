package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Statistics;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.StatisticsService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final UserService userService;

    public StatisticsController(StatisticsService statisticsService, UserService userService) {
        this.statisticsService = statisticsService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Statistics>> getAllStatistics(@RequestHeader("Authorization") String token) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isPresent() && currentUser.get().isAdmin()) {
            List<Statistics> statsList = statisticsService.findAll();
            return ResponseEntity.ok(statsList);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statistics> getStatisticsById(@RequestHeader("Authorization") String token,
                                                        @PathVariable Long id) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Statistics> statOpt = statisticsService.findById(id);
        if (statOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Statistics stats = statOpt.get();
        if (currentUser.get().isAdmin() || currentUser.get().getId().equals(stats.getUser().getId())) {
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Statistics> getStatisticsByUserId(@RequestHeader("Authorization") String token,
                                                            @PathVariable Long userId) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!currentUser.get().isAdmin() && !currentUser.get().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Statistics> optStats = statisticsService.findByUserId(userId);
        if (optStats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(optStats.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStatistics(@RequestHeader("Authorization") String token,
                                           @RequestBody Statistics statistics) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isEmpty() || !currentUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        Statistics savedStats = statisticsService.saveStatistics(statistics);
        return ResponseEntity.ok(savedStats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatistics(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id,
                                              @RequestBody Statistics updatedStats) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isEmpty() || !currentUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        Optional<Statistics> statOpt = statisticsService.findById(id);
        if (statOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Статистика не найдена");
        }
        Statistics existingStats = statOpt.get();
        existingStats.setCompletedLessons(updatedStats.getCompletedLessons());
        existingStats.setCompletedTests(updatedStats.getCompletedTests());
        existingStats.setAchievementsReceived(updatedStats.getAchievementsReceived());
        existingStats.setTrainingDays(updatedStats.getTrainingDays());
        Statistics savedStats = statisticsService.saveStatistics(existingStats);
        return ResponseEntity.ok(savedStats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatistics(@RequestHeader("Authorization") String token,
                                                   @PathVariable Long id) {
        Optional<Users> currentUser = JwtUtil.getUserFromToken(token, userService);
        if (currentUser.isEmpty() || !currentUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        boolean deleted = statisticsService.deleteStatistics(id);
        if (deleted) {
            return ResponseEntity.ok("Статистика удалена");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Статистика не найдена");
    }
}
