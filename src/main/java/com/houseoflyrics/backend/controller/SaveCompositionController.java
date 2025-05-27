package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.SaveComposition;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.SaveCompositionService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/saveComposition")
public class SaveCompositionController {
    private final SaveCompositionService saveCompositionService;
    private final UserService userService;

    public SaveCompositionController(SaveCompositionService saveCompositionService, UserService userService) {
        this.saveCompositionService = saveCompositionService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<SaveComposition>> getAll(@RequestHeader("Authorization") String token) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(saveCompositionService.findAll());
        }
        return ResponseEntity.ok(saveCompositionService.findByUser(user.get().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaveComposition> getById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<SaveComposition> saveComposition = saveCompositionService.findById(id);
        if (saveComposition.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.get().isAdmin() || saveComposition.get().getUser().getId().equals(user.get().getId())) {
            return ResponseEntity.ok(saveComposition.get());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/byFolder")
    public ResponseEntity<List<SaveComposition>> getByFolder(@RequestHeader("Authorization") String token, @RequestParam String folder) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(saveCompositionService.findByFolder(folder));
        }
        List<SaveComposition> filteredCompositions = saveCompositionService.findByFolder(folder)
                .stream()
                .filter(sc -> sc.getUser().getId().equals(user.get().getId()))
                .toList();
        return ResponseEntity.ok(filteredCompositions);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addSaveComposition(@RequestHeader("Authorization") String token, @RequestBody SaveComposition saveComposition) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!user.get().isAdmin()) {
            saveComposition.setUser(user.get());
        }
        SaveComposition savedComposition = saveCompositionService.saveComposition(saveComposition);
        return ResponseEntity.ok(savedComposition);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSaveComposition(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody SaveComposition updatedComposition) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<SaveComposition> existingCompositionOpt = saveCompositionService.findById(id);
        if (existingCompositionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        SaveComposition existingComposition = existingCompositionOpt.get();
        if (!user.get().isAdmin() && !existingComposition.getUser().getId().equals(user.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        existingComposition.setComposition(updatedComposition.getComposition());
        existingComposition.setFolder(updatedComposition.getFolder());

        SaveComposition savedComposition = saveCompositionService.saveComposition(existingComposition);
        return ResponseEntity.ok(savedComposition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSaveComposition(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<SaveComposition> saveCompositionOpt = saveCompositionService.findById(id);
        if (saveCompositionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!user.get().isAdmin() && !saveCompositionOpt.get().getUser().getId().equals(user.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        boolean deleted = saveCompositionService.deleteComposition(id);
        return deleted ? ResponseEntity.ok("Запись удалена") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
