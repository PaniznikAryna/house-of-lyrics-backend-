package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.TestStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.TestStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/testStatus")
public class TestStatusController {

    private final TestStatusService testStatusService;
    private final UserService userService;

    public TestStatusController(TestStatusService testStatusService, UserService userService) {
        this.testStatusService = testStatusService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TestStatus>> getAllTestStatus(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() && tokenUser.get().isAdmin()) {
            List<TestStatus> testStatuses = testStatusService.findAll();
            return ResponseEntity.ok(testStatuses);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestStatus> getTestStatusById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<TestStatus> optTestStatus = testStatusService.findById(id);
        if (optTestStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TestStatus testStatus = optTestStatus.get();
        Users currentUser = tokenUser.get();
        if (currentUser.isAdmin() || currentUser.getId().equals(testStatus.getUser().getId())) {
            return ResponseEntity.ok(testStatus);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<TestStatus>> getMyTestStatus(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        List<TestStatus> testStatuses = testStatusService.findByUserId(currentUser.getId());
        return ResponseEntity.ok(testStatuses);
    }

    @PostMapping("/add")
    public ResponseEntity<TestStatus> addTestStatus(@RequestHeader("Authorization") String token, @RequestBody TestStatus testStatus) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty() || !tokenUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TestStatus savedTestStatus = testStatusService.saveTestStatus(testStatus);
        return ResponseEntity.ok(savedTestStatus);
    }
}
