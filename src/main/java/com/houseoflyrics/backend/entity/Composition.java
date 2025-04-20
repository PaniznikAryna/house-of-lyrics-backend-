package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "composition")
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "tonality", nullable = false, length = 10)
    private String tonality;

    @Column(name = "difficulty_level", nullable = false, length = 50)
    private String difficultyLevel;

    @Column(name = "file_music_original", nullable = false, length = 255)
    private String fileMusicOriginal;

    @Column(name = "picture", nullable = false, length = 255)
    private String picture;

    @ManyToOne
    @JoinColumn(name = "id_composer", nullable = false)
    private Composer composer;

    public Composition(){
    }

    public Composition(String title, String tonality, String difficultyLevel, String fileMusicOriginal, Composer composer, String picture) {
        this.title = title;
        this.tonality = tonality;
        this.difficultyLevel = difficultyLevel;
        this.fileMusicOriginal = fileMusicOriginal;
        this.composer = composer;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTonality() {
        return tonality;
    }

    public void setTonality(String tonality) {
        this.tonality = tonality;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getFileMusicOriginal() {
        return fileMusicOriginal;
    }

    public void setFileMusicOriginal(String fileMusicOriginal) {
        this.fileMusicOriginal = fileMusicOriginal;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Composer getComposer() {
        return composer;
    }

    public void setComposer(Composer composer) {
        this.composer = composer;
    }
}
