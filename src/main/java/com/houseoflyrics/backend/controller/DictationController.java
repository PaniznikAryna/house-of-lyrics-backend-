package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Dictation;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.DictationService;
import com.houseoflyrics.backend.service.DictationStatusService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dictation")
public class DictationController {
    private final DictationService dictationService;
    private final UserService userService;
    private final DictationStatusService dictationStatusService;

    public DictationController(DictationService dictationService, UserService userService, DictationStatusService dictationStatusService) {
        this.dictationService = dictationService;
        this.userService = userService;
        this.dictationStatusService = dictationStatusService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dictation>> getAllDictation(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<Dictation> dictations = dictationService.findAll();
            return ResponseEntity.ok(dictations);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dictation> getDictationById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Dictation> dictation = dictationService.findById(id);
            return dictation.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDictation(@RequestHeader("Authorization") String token,
                                          @RequestBody Dictation dictation) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Dictation savedDictation = dictationService.saveDictation(dictation);
            dictationStatusService.assignDefaultStatusToAllUsers(savedDictation);
            return ResponseEntity.ok(savedDictation);
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDictation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Dictation dictation) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Dictation> existingDictationOpt = dictationService.findById(id);
            if (existingDictationOpt.isPresent()) {
                Dictation existingDictation = existingDictationOpt.get();
                existingDictation.setText(dictation.getText());
                existingDictation.setComposition(dictation.getComposition());
                Dictation savedDictation = dictationService.saveDictation(existingDictation);
                return ResponseEntity.ok(savedDictation);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDictation(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = dictationService.deleteDictation(id);
            return deleted ? ResponseEntity.ok("Инструмент удалён") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }
}
