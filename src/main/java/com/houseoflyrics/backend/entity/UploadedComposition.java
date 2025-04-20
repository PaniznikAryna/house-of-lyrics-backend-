package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_composition")
public class UploadedComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "file", nullable = false, length = 255)
    private String file;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "upload_date", nullable = false, updatable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();

    @Column(name = "picture", length = 255)
    private String picture;

    public UploadedComposition(){
    }

    public UploadedComposition(User user, String file, String title, LocalDateTime uploadDate, String picture) {
        this.user = user;
        this.file = file;
        this.title = title;
        this.uploadDate = uploadDate;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
