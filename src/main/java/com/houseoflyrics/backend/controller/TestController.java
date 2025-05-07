package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Lesson;
import com.houseoflyrics.backend.entity.Test;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.LessonService;
import com.houseoflyrics.backend.service.TestService;
import com.houseoflyrics.backend.service.TestStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;
    private final UserService userService;
    private final TestStatusService testStatusService;
    private final LessonService lessonService;

    public TestController(TestService testService, UserService userService, TestStatusService testStatusService, LessonService lessonService) {
        this.testService = testService;
        this.userService = userService;
        this.testStatusService = testStatusService;
        this.lessonService = lessonService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Test>> getAllTest(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<Test> test = testService.findAll();
            return ResponseEntity.ok(test);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Test> test = testService.findById(id);
            return test.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Test>> getTestsByLesson(@RequestHeader("Authorization") String token, @PathVariable Long lessonId) {
        if (JwtUtil.validateToken(token)) {
            List<Test> tests = testService.findAllByLessonId(lessonId);
            return ResponseEntity.ok(tests);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTest(@RequestHeader("Authorization") String token,
                                     @RequestBody Test test) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            if (test.getLesson() != null && test.getLesson().getId() != null) {
                Optional<Lesson> managedLessonOpt = lessonService.findById(test.getLesson().getId());
                if (managedLessonOpt.isPresent()) {
                    test.setLesson(managedLessonOpt.get());
                } else {
                    return ResponseEntity.badRequest().body("Урок с указанным id не найден");
                }
            } else {
                return ResponseEntity.badRequest().body("Не указан урок для теста");
            }
            Test savedTest = testService.saveTest(test);
            testStatusService.assignDefaultStatusToAllUsers(savedTest);
            return ResponseEntity.ok(savedTest);
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTest(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Test test) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Test> existingTestOpt = testService.findById(id);
            if (existingTestOpt.isPresent()) {
                Test existingTest = existingTestOpt.get();
                existingTest.setLesson(test.getLesson());
                existingTest.setTitle(test.getTitle());
                existingTest.setDescription(test.getDescription());
                Test savedTest = testService.saveTest(existingTest);
                return ResponseEntity.ok(savedTest);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTest(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = testService.deleteTest(id);
            return deleted ? ResponseEntity.ok("Тест удалён") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }
}
