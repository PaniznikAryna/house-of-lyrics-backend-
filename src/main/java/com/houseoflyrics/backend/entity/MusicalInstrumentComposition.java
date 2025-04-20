package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "musical_instrument_composition")
public class MusicalInstrumentComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_musical_instrument", nullable = false)
    private MusicalInstrument musicalInstrument;

    @ManyToOne
    @JoinColumn(name = "id_composition", nullable = false)
    private Composition composition;

    @Column(name = "file_text", nullable = false)
    private String fileText;

    @Column(name = "file_composition", nullable = false)
    private String fileComposition;

    public MusicalInstrumentComposition(){
    }

    public MusicalInstrumentComposition(MusicalInstrument musicalInstrument, Composition composition, String fileComposition, String fileText) {
        this.musicalInstrument = musicalInstrument;
        this.composition = composition;
        this.fileComposition = fileComposition;
        this.fileText = fileText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MusicalInstrument getMusicalInstrument() {
        return musicalInstrument;
    }

    public void setMusicalInstrument(MusicalInstrument musicalInstrument) {
        this.musicalInstrument = musicalInstrument;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public String getFileText() {
        return fileText;
    }

    public void setFileText(String fileText) {
        this.fileText = fileText;
    }

    public String getFileComposition() {
        return fileComposition;
    }

    public void setFileComposition(String fileComposition) {
        this.fileComposition = fileComposition;
    }
}
