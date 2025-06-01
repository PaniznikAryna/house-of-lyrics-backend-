package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.AnswerOption;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.AnswerOptionService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/answerOption")
public class AnswerOptionController {
    private final AnswerOptionService answerOptionService;
    private final UserService userService;

    public AnswerOptionController(AnswerOptionService answerOptionService, UserService userService) {
        this.answerOptionService = answerOptionService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnswerOption>> getAllAnswerOptions(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<AnswerOption> answerOptions = answerOptionService.findAll();
            return ResponseEntity.ok(answerOptions);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerOption> getAnswerOptionById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<AnswerOption> answerOption = answerOptionService.findById(id);
            return answerOption.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/byTask/{taskId}")
    public ResponseEntity<List<AnswerOption>> getAnswerOptionsByTask(@PathVariable Long taskId) {
        List<AnswerOption> answerOptions = answerOptionService.findByTaskId(taskId);
        return ResponseEntity.ok(answerOptions);
    }

    @GetMapping("/correct/byTask/{taskId}")
    public ResponseEntity<List<AnswerOption>> getCorrectAnswerOptionsByTask(@PathVariable Long taskId) {
        List<AnswerOption> correctAnswers = answerOptionService.findCorrectByTaskId(taskId);
        return ResponseEntity.ok(correctAnswers);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addAnswerOption(@RequestHeader("Authorization") String token, @RequestBody AnswerOption answerOption) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            AnswerOption savedAnswerOption = answerOptionService.saveAnswerOption(answerOption);
            return ResponseEntity.ok(savedAnswerOption);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswerOption(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody AnswerOption updatedAnswerOption) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<AnswerOption> existingAnswerOptionOpt = answerOptionService.findById(id);
            if (existingAnswerOptionOpt.isPresent()) {
                AnswerOption existingAnswerOption = existingAnswerOptionOpt.get();
                existingAnswerOption.setTextOfOption(updatedAnswerOption.getTextOfOption());
                existingAnswerOption.setTask(updatedAnswerOption.getTask());
                existingAnswerOption.setCorrect(updatedAnswerOption.isCorrect());
                AnswerOption savedAnswerOption = answerOptionService.saveAnswerOption(existingAnswerOption);
                return ResponseEntity.ok(savedAnswerOption);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnswerOption(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = answerOptionService.deleteAnswerOption(id);
            return deleted ? ResponseEntity.ok("Вариант ответа удалён") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
