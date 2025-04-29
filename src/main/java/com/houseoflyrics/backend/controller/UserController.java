package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() && tokenUser.get().isAdmin()) {
            List<Users> users = userService.findAll();
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() &&
                (tokenUser.get().getId().equals(id) || tokenUser.get().isAdmin())) {
            Optional<Users> userOpt = userService.findById(id);
            return userOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/mail")
    public ResponseEntity<Users> findUserByMail(@RequestHeader("Authorization") String token,
                                                @RequestParam String mail) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() &&
                (tokenUser.get().getMail().equals(mail) || tokenUser.get().isAdmin())) {
            Optional<Users> userOpt = userService.findByMail(mail);
            return userOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(403).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token,
                                             @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() &&
                (tokenUser.get().isAdmin() || tokenUser.get().getId().equals(id))) {
            boolean deleted = userService.deleteUser(id);
            if (deleted)
                return ResponseEntity.ok("Пользователь удалён");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token,
                                        @PathVariable Long id,
                                        @RequestBody Users updatedData) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(401).body("Невалидный токен");
        }

        Users currentUser = tokenUser.get();
        Optional<Users> targetOpt = userService.findById(id);
        if (targetOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        Users updatedUser = userService.updateUser(currentUser, targetOpt.get(), updatedData);
        return ResponseEntity.ok(updatedUser);
    }
}
