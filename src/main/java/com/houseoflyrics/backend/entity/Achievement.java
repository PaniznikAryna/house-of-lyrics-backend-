package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conditions_of_receipt", nullable = false, columnDefinition = "TEXT")
    private String conditionsOfReceipt;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "picture", nullable = false, length = 255)
    private String picture;

    public Achievement(){
    }

    public Achievement(String conditionsOfReceipt, String title, String picture) {
        this.conditionsOfReceipt = conditionsOfReceipt;
        this.title = title;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionsOfReceipt() {
        return conditionsOfReceipt;
    }

    public void setConditionsOfReceipt(String conditionsOfReceipt) {
        this.conditionsOfReceipt = conditionsOfReceipt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
