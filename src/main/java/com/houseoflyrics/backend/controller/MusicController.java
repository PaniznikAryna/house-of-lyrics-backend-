package com.houseoflyrics.backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/music")
public class MusicController {

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> serveMusic(@PathVariable String filename) throws IOException {
        // Берем файл из папки static/music
        Resource music = new ClassPathResource("static/music/" + filename);
        // При условии, что файлы MP3, устанавливаем MIME-тип "audio/mpeg"
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(music);
    }
}
