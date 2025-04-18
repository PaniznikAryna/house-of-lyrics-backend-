package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.User;
import com.houseoflyrics.backend.service.UserService;
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
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String mail, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticate(mail, password);
        return isAuthenticated ? ResponseEntity.ok("Успешный вход!") : ResponseEntity.status(401).body("Ошибка авторизации");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Тест работает!");
    }

}
