package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "save_composition")
public class SaveComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_composition", nullable = false)
    private Composition composition;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @Column(name = "folder", nullable = false, length = 100)
    private String folder;

    public SaveComposition(){
    }

    public SaveComposition(Composition composition, Users user, String folder) {
        this.composition = composition;
        this.user = user;
        this.folder = folder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
