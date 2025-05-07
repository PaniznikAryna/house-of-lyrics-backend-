package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Lesson;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.LessonService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;
    private final UserService userService;

    public LessonController(LessonService lessonService, UserService userService) {
        this.lessonService = lessonService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Lesson>> getAllLessons(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<Lesson> lessons = lessonService.findAll();
            return ResponseEntity.ok(lessons);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Lesson> lesson = lessonService.findById(id);
            return lesson.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@RequestHeader("Authorization") String token, @PathVariable Long courseId) {
        if (JwtUtil.validateToken(token)) {
            List<Lesson> lessons = lessonService.findAllByCourseId(courseId);
            return ResponseEntity.ok(lessons);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLesson(@RequestHeader("Authorization") String token, @RequestBody Lesson lesson) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Lesson savedLesson = lessonService.saveLesson(lesson);
            return ResponseEntity.ok(savedLesson);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Lesson updatedLesson) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Lesson> existingLessonOpt = lessonService.findById(id);
            if (existingLessonOpt.isPresent()) {
                Lesson existingLesson = existingLessonOpt.get();
                existingLesson.setTitle(updatedLesson.getTitle());
                existingLesson.setCourse(updatedLesson.getCourse());
                existingLesson.setContent(updatedLesson.getContent());
                existingLesson.setPicture(updatedLesson.getPicture());
                Lesson savedLesson = lessonService.saveLesson(existingLesson);
                return ResponseEntity.ok(savedLesson);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = lessonService.deleteLesson(id);
            return deleted ? ResponseEntity.ok("Урок удалён") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
