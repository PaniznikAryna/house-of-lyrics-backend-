package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_course", nullable = false)
    private Course course;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "picture", length = 255)
    private String picture;

    public Lesson() {
    }

    public Lesson(String title, Course course, String content, String picture) {
        this.title = title;
        this.course = course;
        this.content = content;
        this.picture = picture;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
}
