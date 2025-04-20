package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_composition_status")
public class TestCompositionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_test_composition", nullable = false)
    private TestComposition testComposition;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public TestCompositionStatus(){
    }

    public TestCompositionStatus(TestComposition testComposition, User user) {
        this.testComposition = testComposition;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestComposition getTestComposition() {
        return testComposition;
    }

    public void setTestComposition(TestComposition testComposition) {
        this.testComposition = testComposition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
