package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.MusicalInstrument;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.MusicalInstrumentService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/musicalInstrument")
public class MusicalInstrumentController {
    private final MusicalInstrumentService musicalInstrumentService;
    private final UserService userService;

    public MusicalInstrumentController(MusicalInstrumentService musicalInstrumentService, UserService userService) {
        this.musicalInstrumentService = musicalInstrumentService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MusicalInstrument>> getAllInstruments(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            List<MusicalInstrument> instruments = musicalInstrumentService.findAll();
            return ResponseEntity.ok(instruments);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicalInstrument> getInstrumentById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (JwtUtil.validateToken(token)) {
            Optional<MusicalInstrument> instrument = musicalInstrumentService.findById(id);
            return instrument.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/search")
    public ResponseEntity<MusicalInstrument> findInstrumentByName(
            @RequestHeader("Authorization") String token,
            @RequestParam String name) {
        if (JwtUtil.validateToken(token)) {
            Optional<MusicalInstrument> instrument = musicalInstrumentService.findByInstrument(name);
            return instrument
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(401).build();
    }


    @PostMapping("/add")
    public ResponseEntity<?> addInstrument(@RequestHeader("Authorization") String token, @RequestBody MusicalInstrument instrument) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            MusicalInstrument savedInstrument = musicalInstrumentService.saveInstrument(instrument);
            return ResponseEntity.ok(savedInstrument);
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInstrument(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody MusicalInstrument updatedInstrument) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<MusicalInstrument> existingInstrumentOpt = musicalInstrumentService.findById(id);
            if (existingInstrumentOpt.isPresent()) {
                MusicalInstrument existingInstrument = existingInstrumentOpt.get();
                existingInstrument.setInstrument(updatedInstrument.getInstrument());
                existingInstrument.setDescription(updatedInstrument.getDescription());
                MusicalInstrument savedInstrument = musicalInstrumentService.saveInstrument(existingInstrument);
                return ResponseEntity.ok(savedInstrument);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstrument(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = musicalInstrumentService.deleteInstrument(id);
            return deleted ? ResponseEntity.ok("Инструмент удалён") : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Доступ запрещён");
    }
}
