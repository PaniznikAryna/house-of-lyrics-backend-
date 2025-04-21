package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mark")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_composition", nullable = false)
    private Composition composition;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @Column(name = "user_note", nullable = false, length = 100)
    private String userNote;

    @Column(name = "tag_time", nullable = false)
    private int tagTime;

    public Mark(){
    }

    public Mark(Composition composition, Users user, String userNote, int tagTime) {
        this.composition = composition;
        this.user = user;
        this.userNote = userNote;
        this.tagTime = tagTime;
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

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public int getTagTime() {
        return tagTime;
    }

    public void setTagTime(int tagTime) {
        this.tagTime = tagTime;
    }
}
