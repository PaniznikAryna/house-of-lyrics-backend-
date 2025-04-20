package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_option")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_task", nullable = false)
    private Task task;

    @Column(name = "text_of_option", nullable = false, length = 255)
    private String textOfOption;

    @Column(name = "correct", nullable = false)
    private boolean correct;

    public AnswerOption(){
    }

    public AnswerOption(Task task, String textOfOption, boolean correct) {
        this.task = task;
        this.textOfOption = textOfOption;
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextOfOption() {
        return textOfOption;
    }

    public void setTextOfOption(String textOfOption) {
        this.textOfOption = textOfOption;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
