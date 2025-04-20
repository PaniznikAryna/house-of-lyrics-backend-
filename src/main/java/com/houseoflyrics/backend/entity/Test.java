package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_lesson", nullable = false, unique = true)
    private Lesson lesson;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "dictation", nullable = false, columnDefinition = "TEXT")
    private String dictation;

    public Test(){
    }

    public Test(Lesson lesson, String title, String dictation) {
        this.lesson = lesson;
        this.title = title;
        this.dictation = dictation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDictation() {
        return dictation;
    }

    public void setDictation(String dictation) {
        this.dictation = dictation;
    }
}
