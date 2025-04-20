package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "achievement_status")
public class AchievementStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_achievement", nullable = false)
    private Achievement achievement;

    @Column(name = "status", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status;

    @Column(name = "date_of_receipt", updatable = false, nullable = true)
    private LocalDateTime dateOfReceipt;

    public AchievementStatus(){
    }

    public AchievementStatus(User user, Achievement achievement, boolean status, LocalDateTime dateOfReceipt) {
        this.user = user;
        this.achievement = achievement;
        this.status = status;
        this.dateOfReceipt = dateOfReceipt;
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

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public LocalDateTime getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(LocalDateTime dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
