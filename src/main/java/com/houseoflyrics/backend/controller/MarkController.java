package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Mark;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.MarkService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mark")
public class MarkController {
    private final MarkService markService;
    private final UserService userService;

    public MarkController(MarkService markService, UserService userService) {
        this.markService = markService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Mark>> getAllMarks(@RequestHeader("Authorization") String token) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(markService.findAll());
        }
        return ResponseEntity.ok(markService.findByUser(user.get().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mark> getMarkById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Mark> mark = markService.findById(id);
        if (mark.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.get().isAdmin() || mark.get().getUser().getId().equals(user.get().getId())) {
            return ResponseEntity.ok(mark.get());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/byComposition/{compositionId}")
    public ResponseEntity<List<Mark>> getMarksByComposition(@RequestHeader("Authorization") String token, @PathVariable Long compositionId) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(markService.findByComposition(compositionId));
        }
        List<Mark> filteredMarks = markService.findByComposition(compositionId)
                .stream()
                .filter(m -> m.getUser().getId().equals(user.get().getId()))
                .toList();
        return ResponseEntity.ok(filteredMarks);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMark(@RequestHeader("Authorization") String token, @RequestBody Mark mark) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Если пользователь не админ, принудительно устанавливаем его ID
        if (!user.get().isAdmin()) {
            mark.setUser(user.get());
        }

        Mark savedMark = markService.saveMark(mark);
        return ResponseEntity.ok(savedMark);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMark(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Mark updatedMark) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Mark> existingMarkOpt = markService.findById(id);
        if (existingMarkOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Mark existingMark = existingMarkOpt.get();

        if (!user.get().isAdmin() && !existingMark.getUser().getId().equals(user.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }

        existingMark.setComposition(updatedMark.getComposition());
        existingMark.setUserNote(updatedMark.getUserNote());
        existingMark.setTagTime(updatedMark.getTagTime());

        Mark savedMark = markService.saveMark(existingMark);
        return ResponseEntity.ok(savedMark);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMark(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Mark> markOpt = markService.findById(id);
        if (markOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.get().isAdmin() || markOpt.get().getUser().getId().equals(user.get().getId())) {
            boolean deleted = markService.deleteMark(id);
            return deleted ? ResponseEntity.ok("Метка удалена") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
