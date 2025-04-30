package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.DictationStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.DictationStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dictationStatus")
public class DictationStatusController {

    private final DictationStatusService dictationStatusService;
    private final UserService userService;

    public DictationStatusController(DictationStatusService dictationStatusService, UserService userService) {
        this.dictationStatusService = dictationStatusService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DictationStatus>> getAllDictationStatus(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() && tokenUser.get().isAdmin()) {
            List<DictationStatus> dictationStatuses = dictationStatusService.findAll();
            return ResponseEntity.ok(dictationStatuses);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DictationStatus> getDictationStatusById(@RequestHeader("Authorization") String token,
                                                                  @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<DictationStatus> optDictationStatus = dictationStatusService.findById(id);
        if (optDictationStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        DictationStatus dictationStatus = optDictationStatus.get();
        Users currentUser = tokenUser.get();
        if (currentUser.isAdmin() || currentUser.getId().equals(dictationStatus.getUser().getId())) {
            return ResponseEntity.ok(dictationStatus);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<DictationStatus>> getMyDictationStatus(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        List<DictationStatus> dictationStatuses = dictationStatusService.findByUserId(currentUser.getId());
        return ResponseEntity.ok(dictationStatuses);
    }

    @PostMapping("/add")
    public ResponseEntity<DictationStatus> addDictationStatus(@RequestHeader("Authorization") String token,
                                                              @RequestBody DictationStatus dictationStatus) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty() || !tokenUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        DictationStatus savedDictationStatus = dictationStatusService.saveDictationStatus(dictationStatus);
        return ResponseEntity.ok(savedDictationStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDictationStatus(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody DictationStatus updatedData) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty() || !tokenUser.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        Optional<DictationStatus> optDictationStatus = dictationStatusService.findById(id);
        if (optDictationStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Статус не найден");
        }
        DictationStatus existingDictationStatus = optDictationStatus.get();
        existingDictationStatus.setDictation(updatedData.getDictation());
        existingDictationStatus.setStatus(updatedData.getStatus());
        existingDictationStatus.setResult(updatedData.getResult());
        DictationStatus savedDictationStatus = dictationStatusService.saveDictationStatus(existingDictationStatus);
        return ResponseEntity.ok(savedDictationStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDictationStatus(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидный токен");
        }
        Users currentUser = tokenUser.get();
        Optional<DictationStatus> optDictationStatus = dictationStatusService.findById(id);
        if (optDictationStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Статус не найден");
        }
        DictationStatus dictationStatus = optDictationStatus.get();
        if (currentUser.isAdmin() || currentUser.getId().equals(dictationStatus.getUser().getId())) {
            boolean deleted = dictationStatusService.deleteDictationStatus(id);
            if (deleted) {
                return ResponseEntity.ok("Статус удалён");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении статуса");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @GetMapping("/dictationStatusCount")
    public ResponseEntity<Long> getPassedDictationCount(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        Users currentUser = tokenUser.get();
        long passedCount = dictationStatusService.countByUserAndStatus(currentUser, DictationStatus.DictationStatusEnum.ПРОЙДЕНО);
        return ResponseEntity.ok(passedCount);
    }
}
