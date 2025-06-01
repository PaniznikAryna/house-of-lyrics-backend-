package com.houseoflyrics.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @PostMapping("/profile-photo")
    public ResponseEntity<String> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/profil/";
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Ошибка: не удалось создать папку " + dir.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания директории для загрузки.");
        }
        if (file.isEmpty()) {
            System.out.println("Ошибка: файл пустой.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл не загружен.");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = UUID.randomUUID().toString();
        }
        File destination = new File(dir, originalFilename);
        try {
            System.out.println("Сохраняем фото: " + destination.getAbsolutePath());
            file.transferTo(destination);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка загрузки фото");
        }
        String fileUrl = "/api/images/profil/" + originalFilename;
        System.out.println("Файл загружен успешно: " + fileUrl);
        return ResponseEntity.ok(fileUrl);
    }

    // GET-метод остается для диагностики, возвращающий 405, поскольку загрузка должна идти через POST
    @GetMapping("/profile-photo")
    public ResponseEntity<String> profilePhotoGet() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("GET метод не поддерживается для загрузки файлов. Используйте POST.");
    }

    @PostMapping("/music")
    public ResponseEntity<String> uploadMusic(@RequestParam("file") MultipartFile file) {
        // Абсолютный путь для хранения музыкальных файлов в папке static/music:
        String uploadDir = System.getProperty("user.dir")
                + "/src/main/resources/static/music/";
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Ошибка: не удалось создать папку для музыки " + dir.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания директории для музыки.");
        }

        if (file.isEmpty()) {
            System.out.println("Ошибка: файл пустой.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл не загружен.");
        }

        // Генерируем уникальное имя файла, сохраняя расширение
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        File destination = new File(dir, uniqueFileName);

        try {
            System.out.println("Сохраняем музыкальный файл в: " + destination.getAbsolutePath());
            file.transferTo(destination);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка загрузки музыкального файла");
        }

        // Формируем URL, по которому файл будет доступен. Например, если у вас настроен context-путь /api:
        String fileUrl = "/music/" + uniqueFileName;
        System.out.println("Музыкальный файл загружен успешно: " + fileUrl);
        return ResponseEntity.ok(fileUrl);
    }

}
