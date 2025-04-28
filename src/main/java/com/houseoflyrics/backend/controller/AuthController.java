package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        if (userService.findByMail(user.getMail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Пользователь с данной почтой уже существует");
        }
        Users registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String mail, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticate(mail, password);
        if (isAuthenticated) {
            String token = JwtUtil.generateToken(mail); // Генерация токена
            return ResponseEntity.ok("Токен: " + token);
        } else {
            return ResponseEntity.status(401).body("Ошибка авторизации");
        }
    }


}
