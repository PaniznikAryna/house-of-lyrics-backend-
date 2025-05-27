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
    private Users user;

    @Column(name = "result", nullable = false)
    private double result = 0.0;

    public TestCompositionStatus(){
    }

    public TestCompositionStatus(TestComposition testComposition, Users user, double result) {
        this.testComposition = testComposition;
        this.user = user;
        this.result = result;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
