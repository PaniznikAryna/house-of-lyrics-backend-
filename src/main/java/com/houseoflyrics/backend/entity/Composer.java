package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "composer")
public class Composer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "era", nullable = false, length = 100)
    private String era;

    @Column(name = "biography", nullable = false, columnDefinition = "TEXT")
    private String biography;

    @Column(name = "photo", nullable = false, length = 255)
    private String photo;

    public Composer(){
    }

    public Composer(String name, String country, String era, String biography, String photo) {
        this.name = name;
        this.country = country;
        this.era = era;
        this.biography = biography;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
