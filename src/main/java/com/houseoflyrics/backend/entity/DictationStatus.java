package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dictation_status",        uniqueConstraints = @UniqueConstraint(columnNames = {"id_dictation", "id_user"}))
public class DictationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dictation", nullable = false)
    private Dictation dictation;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public enum DictationStatusEnum{
        НЕ_НАЧАТО("не начато"),
        НЕ_ПРОЙДЕНО("не пройдено"),
        В_ПРОЦЕССЕ("в процессе"),
        ПРОЙДЕНО("пройдено");

        private final String value;

        DictationStatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DictationStatus.DictationStatusEnum status;

    @Column(name = "result", nullable = false)
    private double result = 0.0;

    public DictationStatus(){
    }

    public DictationStatus(Dictation dictation, User user, DictationStatusEnum status, double result) {
        this.dictation = dictation;
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

    public Dictation getDictation() {
        return dictation;
    }

    public void setDictation(Dictation dictation) {
        this.dictation = dictation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DictationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DictationStatusEnum status) {
        this.status = status;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
