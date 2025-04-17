package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "musical_instrument")
public class Musical_instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instrument", nullable = false, length = 100)
    private String instrument;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    public Musical_instrument() {}

    public Musical_instrument(String instrument, String description) {
        this.instrument = instrument;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
