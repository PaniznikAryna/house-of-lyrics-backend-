package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.MusicalInstrumentComposition;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.MusicalInstrumentCompositionService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/musicalInstrumentComposition")
public class MusicalInstrumentCompositionController {
    private final MusicalInstrumentCompositionService service;
    private final UserService userService;

    public MusicalInstrumentCompositionController(MusicalInstrumentCompositionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<MusicalInstrumentComposition>> getAll(@RequestHeader("Authorization") String token) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(service.findByMusicalInstrument(user.get().getMusicalInstrument().getId()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MusicalInstrumentComposition> getById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<MusicalInstrumentComposition> composition = service.findById(id);
        if (composition.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.get().isAdmin() || composition.get().getMusicalInstrument().getId().equals(user.get().getMusicalInstrument().getId())) {
            return ResponseEntity.ok(composition.get());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/byInstrument/{instrumentId}")
    public ResponseEntity<List<MusicalInstrumentComposition>> getByInstrument(@RequestHeader("Authorization") String token, @PathVariable Long instrumentId) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(service.findByMusicalInstrument(instrumentId));
        }
        List<MusicalInstrumentComposition> filteredCompositions = service.findByMusicalInstrument(instrumentId)
                .stream()
                .filter(c -> c.getMusicalInstrument().getId().equals(user.get().getMusicalInstrument().getId()))
                .toList();
        return ResponseEntity.ok(filteredCompositions);
    }



    @GetMapping("/byComposition/{compositionId}")
    public ResponseEntity<List<MusicalInstrumentComposition>> getByComposition(@RequestHeader("Authorization") String token, @PathVariable Long compositionId) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.get().isAdmin()) {
            return ResponseEntity.ok(service.findByComposition(compositionId));
        }
        List<MusicalInstrumentComposition> filteredCompositions = service.findByComposition(compositionId)
                .stream()
                .filter(c -> c.getMusicalInstrument().getId().equals(user.get().getMusicalInstrument().getId()))
                .toList();
        return ResponseEntity.ok(filteredCompositions);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addComposition(@RequestHeader("Authorization") String token, @RequestBody MusicalInstrumentComposition composition) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            MusicalInstrumentComposition savedComposition = service.saveComposition(composition);
            return ResponseEntity.ok(savedComposition);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateComposition(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody MusicalInstrumentComposition updatedComposition) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            Optional<MusicalInstrumentComposition> existingCompositionOpt = service.findById(id);
            if (existingCompositionOpt.isPresent()) {
                MusicalInstrumentComposition existingComposition = existingCompositionOpt.get();
                existingComposition.setMusicalInstrument(updatedComposition.getMusicalInstrument());
                existingComposition.setComposition(updatedComposition.getComposition());
                existingComposition.setFileComposition(updatedComposition.getFileComposition());
                existingComposition.setFileText(updatedComposition.getFileText());

                MusicalInstrumentComposition savedComposition = service.saveComposition(existingComposition);
                return ResponseEntity.ok(savedComposition);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComposition(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Optional<Users> user = JwtUtil.getUserFromToken(token, userService);
        if (user.isPresent() && user.get().isAdmin()) {
            boolean deleted = service.deleteComposition(id);
            return deleted ? ResponseEntity.ok("Запись удалена") : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
