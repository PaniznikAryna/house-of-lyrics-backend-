package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

   @Column(name = "completed_lessons", nullable = false)
       private int completedLessons = 0;


   @Column(name = "completed_tests", nullable = false)
       private int completedTests = 0;


   @Column(name = "achievements_received", nullable = false)
       private int achievementsReceived = 0;


   @Column(name = "training_days", nullable = false)
    private int trainingDays = 0;

    public Statistics() {}

    public Statistics(User user, int completedLessons, int completedTests, int achievementsReceived, int trainingDays) {
        this.user = user;
        this.completedLessons = completedLessons;
        this.completedTests = completedTests;
        this.achievementsReceived = achievementsReceived;
        this.trainingDays = trainingDays;
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

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public int getCompletedTests() {
        return completedTests;
    }

    public void setCompletedTests(int completedTests) {
        this.completedTests = completedTests;
    }

    public int getAchievementsReceived() {
        return achievementsReceived;
    }

    public void setAchievementsReceived(int achievementsReceived) {
        this.achievementsReceived = achievementsReceived;
    }

    public int getTrainingDays() {
        return trainingDays;
    }

    public void setTrainingDays(int trainingDays) {
        this.trainingDays = trainingDays;
    }
}
