package com.houseoflyrics.backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {

    @GetMapping("/lessons/{filename}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {
        Resource image = new ClassPathResource("static/images/lessons/" + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
    @GetMapping("/course/{filename}")
    public ResponseEntity<Resource> serveCourseImage(@PathVariable String filename) throws IOException {
        Resource image = new ClassPathResource("static/images/course/" + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("/profil/{filename}")
    public ResponseEntity<Resource> serveProfileImage(@PathVariable String filename) throws IOException {
        Resource image = new ClassPathResource("static/images/profil/" + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("/achivment/{filename}")
    public ResponseEntity<Resource> serveAchievementImage(@PathVariable String filename) throws IOException {
        Resource image = new ClassPathResource("static/images/achivment/" + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }



}
