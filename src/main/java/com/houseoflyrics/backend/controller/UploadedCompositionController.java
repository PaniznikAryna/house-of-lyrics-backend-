package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.UploadedComposition;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.UploadedCompositionService;
import com.houseoflyrics.backend.service.UserService;
import com.houseoflyrics.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/uploadedComposition")
public class UploadedCompositionController {

    private final UploadedCompositionService uploadedCompositionService;
    private final UserService userService;

    public UploadedCompositionController(UploadedCompositionService uploadedCompositionService, UserService userService) {
        this.uploadedCompositionService = uploadedCompositionService;
        this.userService = userService;
    }

    @PostMapping("/my/add")
    public ResponseEntity<?> addMyComposition(@RequestHeader("Authorization") String token,
                                              @RequestParam("file") MultipartFile file,
                                              @RequestParam("title") String title,
                                              @RequestParam(value = "picture", required = false) MultipartFile picture) {
        Optional<Users> tokenUser = JwtUtil.getUserFromToken(token, userService);
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/music/";
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания директории для музыки.");
        }

        String audioFileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        File audioFile = new File(dir, audioFileName);
        try {
            file.transferTo(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка загрузки музыкального файла");
        }

        String pictureFileName = null;
        if (picture != null && !picture.isEmpty()) {
            String pictureDir = System.getProperty("user.dir") + "/src/main/resources/static/images/cover/";
            File pictureFolder = new File(pictureDir);
            if (!pictureFolder.exists() && !pictureFolder.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка создания директории для обложек.");
            }
            pictureFileName = UUID.randomUUID().toString() + getFileExtension(picture.getOriginalFilename());
            File pictureFile = new File(pictureFolder, pictureFileName);
            try {
                picture.transferTo(pictureFile);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка загрузки обложки");
            }
        }

        UploadedComposition composition = new UploadedComposition();
        composition.setTitle(title);
        composition.setFile(audioFileName);
        composition.setPicture(pictureFileName);
        composition.setUser(tokenUser.get());

        UploadedComposition savedComposition = uploadedCompositionService.saveUploadedComposition(composition);
        return ResponseEntity.ok(savedComposition);
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
}
