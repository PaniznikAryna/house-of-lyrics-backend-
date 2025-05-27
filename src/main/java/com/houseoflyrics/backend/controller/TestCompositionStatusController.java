package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.TestCompositionStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.TestCompositionStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/testCompositionStatus")
public class TestCompositionStatusController {

    private final TestCompositionStatusService statusService;
    private final UserService userService;

    public TestCompositionStatusController(TestCompositionStatusService statusService, UserService userService) {
        this.statusService = statusService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TestCompositionStatus>> getAllStatuses(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() && tokenUser.get().isAdmin()) {
            return ResponseEntity.ok(statusService.findAll());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<TestCompositionStatus>> getMyStatuses(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        return ResponseEntity.ok(statusService.findByUser(currentUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCompositionStatus> getStatusById(@RequestHeader("Authorization") String token,
                                                               @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<TestCompositionStatus> optStatus = statusService.findById(id);
        if (optStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TestCompositionStatus status = optStatus.get();
        Users currentUser = tokenUser.get();
        if (currentUser.isAdmin() || currentUser.getId().equals(status.getUser().getId())) {
            return ResponseEntity.ok(status);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/notPassed")
    public ResponseEntity<List<TestCompositionStatus>> getNotPassedStatuses(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        List<TestCompositionStatus> statuses;
        if (currentUser.isAdmin()) {
            statuses = statusService.findNotPassed();
        } else {
            statuses = statusService.findByUser(currentUser.getId())
                    .stream()
                    .filter(status -> status.getResult() < 60.0)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/passed")
    public ResponseEntity<List<TestCompositionStatus>> getPassedStatuses(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        List<TestCompositionStatus> statuses;
        if (currentUser.isAdmin()) {
            statuses = statusService.findPassed();
        } else {
            statuses = statusService.findByUser(currentUser.getId())
                    .stream()
                    .filter(status -> status.getResult() >= 60.0)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(statuses);
    }

    @PostMapping("/add")
    public ResponseEntity<TestCompositionStatus> addStatus(@RequestHeader("Authorization") String token,
                                                           @RequestBody TestCompositionStatus status) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty() || !tokenUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TestCompositionStatus savedStatus = statusService.saveStatus(status);
        return ResponseEntity.ok(savedStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@RequestHeader("Authorization") String token,
                                          @PathVariable Long id,
                                          @RequestBody TestCompositionStatus updatedStatus) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty() || !tokenUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        Optional<TestCompositionStatus> optStatus = statusService.findById(id);
        if (optStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TestCompositionStatus existingStatus = optStatus.get();
        existingStatus.setResult(updatedStatus.getResult());
        TestCompositionStatus savedStatus = statusService.saveStatus(existingStatus);
        return ResponseEntity.ok(savedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatus(@RequestHeader("Authorization") String token,
                                               @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидный токен");
        }
        boolean deleted = statusService.deleteStatus(id);
        if (deleted) {
            return ResponseEntity.ok("Статус удалён");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Статус не найден");
        }
    }
}
