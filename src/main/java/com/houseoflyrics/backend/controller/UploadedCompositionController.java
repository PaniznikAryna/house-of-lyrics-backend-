package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.UploadedComposition;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.UploadedCompositionService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/uploadedComposition")
public class UploadedCompositionController {

    private final UploadedCompositionService uploadedCompositionService;
    private final UserService userService;

    public UploadedCompositionController(UploadedCompositionService uploadedCompositionService, UserService userService) {
        this.uploadedCompositionService = uploadedCompositionService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UploadedComposition>> getAllCompositions(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isPresent() && tokenUser.get().isAdmin()) {
            List<UploadedComposition> compositions = uploadedCompositionService.findAll();
            return ResponseEntity.ok(compositions);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<UploadedComposition>> getMyCompositions(@RequestHeader("Authorization") String token) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        List<UploadedComposition> myCompositions = uploadedCompositionService.findAllByUserId(currentUser.getId());
        return ResponseEntity.ok(myCompositions);
    }


    @GetMapping("/search")
    public ResponseEntity<UploadedComposition> searchCompositionByTitle(
            @RequestHeader("Authorization") String token,
            @RequestParam String title) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users currentUser = tokenUser.get();
        Optional<UploadedComposition> optComposition = uploadedCompositionService.findByUserAndTitle(currentUser, title);
        return optComposition.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UploadedComposition> getCompositionById(@RequestHeader("Authorization") String token,
                                                                  @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<UploadedComposition> optComposition = uploadedCompositionService.findById(id);
        if (optComposition.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UploadedComposition composition = optComposition.get();
        Users currentUser = tokenUser.get();
        if (currentUser.getId().equals(composition.getUser().getId()) || currentUser.isAdmin()) {
            return ResponseEntity.ok(composition);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/add")
    public ResponseEntity<UploadedComposition> addComposition(@RequestHeader("Authorization") String token,
                                                              @RequestBody UploadedComposition composition) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        composition.setUser(tokenUser.get());
        UploadedComposition savedComposition = uploadedCompositionService.saveUploadedComposition(composition);
        return ResponseEntity.ok(savedComposition);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComposition(@RequestHeader("Authorization") String token,
                                               @PathVariable Long id,
                                               @RequestBody UploadedComposition updatedData) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидный токен");
        }
        Users currentUser = tokenUser.get();
        Optional<UploadedComposition> optComposition = uploadedCompositionService.findById(id);
        if (optComposition.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Композиция не найдена");
        }
        UploadedComposition existingComposition = optComposition.get();
        if (!currentUser.getId().equals(existingComposition.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Доступ запрещён: изменять можно только свои композиции");
        }
        existingComposition.setTitle(updatedData.getTitle());
        existingComposition.setFile(updatedData.getFile());
        existingComposition.setPicture(updatedData.getPicture());
        UploadedComposition savedComposition = uploadedCompositionService.saveUploadedComposition(existingComposition);
        return ResponseEntity.ok(savedComposition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComposition(@RequestHeader("Authorization") String token,
                                                    @PathVariable Long id) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидный токен");
        }
        Users currentUser = tokenUser.get();
        Optional<UploadedComposition> optComposition = uploadedCompositionService.findById(id);
        if (optComposition.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Композиция не найдена");
        }
        UploadedComposition composition = optComposition.get();
        if (currentUser.getId().equals(composition.getUser().getId()) || currentUser.isAdmin()) {
            boolean deleted = uploadedCompositionService.deleteUploadedComposition(id);
            if (deleted) {
                return ResponseEntity.ok("Композиция удалена");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при удалении композиции");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
    }
}
