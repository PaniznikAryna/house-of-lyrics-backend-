package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Task;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.TaskService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<Task> tasks = taskService.findAll();
            return ResponseEntity.ok(tasks);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Task> task = taskService.findById(id);
            return task.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/byTest/{testId}")
    public ResponseEntity<List<Task>> getTasksByTest(@PathVariable Long testId) {
        List<Task> tasks = taskService.findByTestId(testId);
        return ResponseEntity.ok(tasks);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestHeader("Authorization") String token, @RequestBody Task task) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Task savedTask = taskService.saveTask(task);
            return ResponseEntity.ok(savedTask);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Task updatedTask) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Task> existingTaskOpt = taskService.findById(id);
            if (existingTaskOpt.isPresent()) {
                Task existingTask = existingTaskOpt.get();
                existingTask.setTaskText(updatedTask.getTaskText());
                existingTask.setTest(updatedTask.getTest());
                Task savedTask = taskService.saveTask(existingTask);
                return ResponseEntity.ok(savedTask);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = taskService.deleteTask(id);
            return deleted ? ResponseEntity.ok("Задание удалено") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
