package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Course;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.CourseService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourse() {
        List<Course> instruments = courseService.findAll();
        return ResponseEntity.ok(instruments);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Course> instrument = courseService.findById(id);
            return instrument.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/search")
    public ResponseEntity<Course> findCourseByTitle(
            @RequestHeader("Authorization") String token,
            @RequestParam String title) {
        if (JwtUtil.validateToken(token)) {
            Optional<Course> course = courseService.findByTitle(title);
            return course
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }


    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestHeader("Authorization") String token, @RequestBody Course course) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Course savedCourse = courseService.saveCourse(course);
            return ResponseEntity.ok(savedCourse);
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Course updatedCourse) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Course> existingCourseOpt = courseService.findById(id);
            if (existingCourseOpt.isPresent()) {
                Course existingCourse = existingCourseOpt.get();
                existingCourse.setTitle(updatedCourse.getTitle());
                existingCourse.setPicture(updatedCourse.getPicture());
                existingCourse.setDescription(updatedCourse.getDescription());
                Course savedCourse = courseService.saveCourse(existingCourse);
                return ResponseEntity.ok(savedCourse);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = courseService.deleteCourse(id);
            return deleted ? ResponseEntity.ok("Курс удалён") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }
}
