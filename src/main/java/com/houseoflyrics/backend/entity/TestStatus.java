package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_status")
public class TestStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_test", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    public enum TestStatusEnum{
        НЕ_НАЧАТО("не начато"),
        НЕ_ПРОЙДЕНО("не пройдено"),
        ПРОЙДЕНО("пройдено"),
        ЗАВЕРШЕНО("завершено");

        private final String value;

        TestStatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Convert(converter = com.houseoflyrics.backend.TestStatusEnumConverter.class)
    @Column(name = "status", nullable = false)
    private TestStatusEnum status;

    @Column(name = "result", nullable = false)
    private double result = 0.0;

    public TestStatus(){
    }

    public TestStatus(Test test, Users user, TestStatusEnum status, double result) {
        this.test = test;
        this.user = user;
        this.status = status;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public TestStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TestStatusEnum status) {
        this.status = status;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }


}
