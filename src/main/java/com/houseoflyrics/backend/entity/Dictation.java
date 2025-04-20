package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dictation")
public class Dictation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "composition", nullable = false, columnDefinition = "TEXT")
    private String composition;

    public Dictation(){
    }

    public Dictation(String text, String composition) {
        this.text = text;
        this.composition = composition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }
}
