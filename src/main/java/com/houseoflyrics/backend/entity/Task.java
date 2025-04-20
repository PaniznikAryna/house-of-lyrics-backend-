package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_test", nullable = false)
    private Test test;

    @Column(name = "task_text", nullable = false, columnDefinition = "TEXT")
    private String taskText;

    public Task(){
    }

    public Task(Test test, String taskText) {
        this.test = test;
        this.taskText = taskText;
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

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }
}
