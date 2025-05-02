package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.Composer;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.ComposerService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/composer")
public class ComposerController {
    private final ComposerService composerService;
    private final UserService userService;

    public ComposerController(ComposerService composerService, UserService userService) {
        this.composerService = composerService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Composer>> getAllComposer(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<Composer> composers = composerService.findAll();
            return ResponseEntity.ok(composers);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Composer> getComposerById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<Composer> composer = composerService.findById(id);
            return composer.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/search")
    public ResponseEntity<Composer> findComposerByName(
            @RequestHeader("Authorization") String token,
            @RequestParam String name) {
        if (JwtUtil.validateToken(token)) {
            Optional<Composer> composer = composerService.findByName(name);
            return composer.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }


    @PostMapping("/add")
    public ResponseEntity<?> addComposer(@RequestHeader("Authorization") String token, @RequestBody Composer composer) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Composer savedComposer = composerService.saveComposer(composer);
            return ResponseEntity.ok(savedComposer);
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComposer(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Composer updatedComposer) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<Composer> existingComposerOpt = composerService.findById(id);
            if (existingComposerOpt.isPresent()) {
                Composer existingComposer = existingComposerOpt.get();
                existingComposer.setName(updatedComposer.getName());
                existingComposer.setCountry(updatedComposer.getCountry());
                existingComposer.setEra(updatedComposer.getEra());
                existingComposer.setBiography(updatedComposer.getBiography());
                existingComposer.setPhoto(updatedComposer.getPhoto());
                Composer savedComposer = composerService.saveComposer(existingComposer);
                return ResponseEntity.ok(savedComposer);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComposer(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = composerService.deleteComposer(id);
            return deleted ? ResponseEntity.ok("Композитор удалён") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }
}
