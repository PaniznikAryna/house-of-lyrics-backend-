package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Statistics;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.StatisticsService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final StatisticsService statisticsService;

    public AuthController(UserService userService, StatisticsService statisticsService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        if (userService.findByMail(user.getMail()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь с данной почтой уже существует");
        }
        user.setAdmin(false);
        Users registeredUser = userService.registerUser(user);

        Statistics newStats = new Statistics(registeredUser, 0, 0, 0, 0);
        Statistics createdStats = statisticsService.saveStatistics(newStats);

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
