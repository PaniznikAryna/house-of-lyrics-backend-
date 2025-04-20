package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_composition")
public class TestComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_composition", nullable = false)
    private Composition composition;

    public TestComposition(){
    }

    public TestComposition(String description, Composition composition) {
        this.description = description;
        this.composition = composition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }
}
