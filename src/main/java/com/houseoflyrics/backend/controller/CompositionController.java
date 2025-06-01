package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Composition;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.CompositionService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/composition")
public class CompositionController {
    private final CompositionService compositionService;
    private final UserService userService;

    public CompositionController(CompositionService compositionService, UserService userService) {
        this.compositionService = compositionService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Composition>> getAllCompositions() {
        List<Composition> compositions = compositionService.findAll();
        return ResponseEntity.ok(compositions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Composition> getCompositionById(@PathVariable Long id) {
        Optional<Composition> composition = compositionService.findById(id);
        return composition.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Composition>> findCompositionsByTitle(@RequestParam String title) {
        List<Composition> compositions = compositionService.findByTitle(title);
        return ResponseEntity.ok(compositions);
    }

    @GetMapping("/byComposer/{composerId}")
    public ResponseEntity<List<Composition>> getCompositionsByComposer(@PathVariable Long composerId) {
        List<Composition> compositions = compositionService.findByComposer(composerId);
        return ResponseEntity.ok(compositions);
    }

    @GetMapping("/byTonality")
    public ResponseEntity<List<Composition>> getCompositionsByTonality(@RequestParam String tonality) {
        List<Composition> compositions = compositionService.findByTonality(tonality);
        return ResponseEntity.ok(compositions);
    }

    @GetMapping("/byDifficulty")
    public ResponseEntity<List<Composition>> getCompositionsByDifficulty(@RequestParam String difficultyLevel) {
        List<Composition> compositions = compositionService.findByDifficultyLevel(difficultyLevel);
        return ResponseEntity.ok(compositions);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComposition(@RequestHeader("Authorization") String token, @RequestBody Composition composition) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Composition savedComposition = compositionService.saveComposition(composition);
            return ResponseEntity.ok(savedComposition);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComposition(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = compositionService.deleteComposition(id);
            return deleted ? ResponseEntity.ok("Композиция удалена") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComposition(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Composition updatedComposition) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Composition> existingCompositionOpt = compositionService.findById(id);
            if (existingCompositionOpt.isPresent()) {
                Composition existingComposition = existingCompositionOpt.get();
                existingComposition.setTitle(updatedComposition.getTitle());
                existingComposition.setTonality(updatedComposition.getTonality());
                existingComposition.setDifficultyLevel(updatedComposition.getDifficultyLevel());
                existingComposition.setFileMusicOriginal(updatedComposition.getFileMusicOriginal());
                existingComposition.setPicture(updatedComposition.getPicture());
                existingComposition.setComposer(updatedComposition.getComposer());

                Composition savedComposition = compositionService.saveComposition(existingComposition);
                return ResponseEntity.ok(savedComposition);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
