package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Composition;
import com.houseoflyrics.backend.entity.TestComposition;
import com.houseoflyrics.backend.entity.TestCompositionStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.CompositionService;
import com.houseoflyrics.backend.service.TestCompositionService;
import com.houseoflyrics.backend.service.TestCompositionStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/testComposition")
public class TestCompositionController {
    private final TestCompositionService testCompositionService;
    private final UserService userService;
    private final TestCompositionStatusService testCompositionStatusService;


    private final CompositionService compositionService;

    public TestCompositionController(TestCompositionService testCompositionService, UserService userService, CompositionService compositionService, TestCompositionStatusService testCompositionStatusService) {
        this.testCompositionService = testCompositionService;
        this.userService = userService;
        this.compositionService = compositionService;
        this.testCompositionStatusService = testCompositionStatusService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<TestComposition>> getAll() {
        return ResponseEntity.ok(testCompositionService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<TestComposition> getById(@PathVariable Long id) {
        Optional<TestComposition> testComposition = testCompositionService.findById(id);
        return testComposition.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/byComposition/{compositionId}")
    public ResponseEntity<List<TestComposition>> getByComposition(@PathVariable Long compositionId) {
        return ResponseEntity.ok(testCompositionService.findByComposition(compositionId));
    }


    @PostMapping("/add")
    public ResponseEntity<?> addTestComposition(@RequestHeader("Authorization") String token,
                                                @RequestBody Map<String, Object> payload) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty() || !tokenUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }

        String description = (String) payload.get("description");
        @SuppressWarnings("unchecked")
        List<Integer> compositionIds = (List<Integer>) payload.get("compositions");

        List<Composition> compositions = compositionIds.stream()
                .map(compId -> compositionService.findById(Long.valueOf(compId))
                        .orElseThrow(() -> new IllegalArgumentException("Композиция с ID " + compId + " не найдена")))
                .toList();

        TestComposition newTestComposition = new TestComposition();
        newTestComposition.setDescription(description);
        newTestComposition.setComposition(compositions.get(0)); // Если поле поддерживает только одну композицию

        TestComposition savedTest = testCompositionService.saveTestComposition(newTestComposition);

        List<Users> allUsers = userService.findAll();
        testCompositionStatusService.assignDefaultStatusToAllUsers(savedTest, allUsers);

        return ResponseEntity.ok(savedTest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTestComposition(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty() || !user.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }

        String description = (String) payload.get("description");
        List<Integer> compositionIds = (List<Integer>) payload.get("compositions");

        Optional<TestComposition> existingTestOpt = testCompositionService.findById(id);
        if (existingTestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        TestComposition existingTest = existingTestOpt.get();
        existingTest.setDescription(description);

        List<Composition> compositions = compositionIds.stream()
                .map(compId -> compositionService.findById(Long.valueOf(compId))
                        .orElseThrow(() -> new IllegalArgumentException("Композиция с ID " + compId + " не найдена")))
                .toList();

        existingTest.setComposition(compositions.get(0)); // Если у тебя только одна композиция

        TestComposition savedTestComposition = testCompositionService.saveTestComposition(existingTest);
        return ResponseEntity.ok(savedTestComposition);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTestComposition(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = testCompositionService.deleteTestComposition(id);
            return deleted ? ResponseEntity.ok("Запись удалена") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
