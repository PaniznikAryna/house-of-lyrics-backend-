package com.houseoflyrics.backend.controller;

import com.houseoflyrics.backend.entity.MusicalInstrument;
import com.houseoflyrics.backend.service.MusicalInstrumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/musicalInstrument")
public class MusicalInstrumentController {
    private final MusicalInstrumentService musicalInstrumentService;

    public MusicalInstrumentController(MusicalInstrumentService musicalInstrumentService) {
        this.musicalInstrumentService = musicalInstrumentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicalInstrument> getInstrumentById(@PathVariable Long id) {
        Optional<MusicalInstrument> instrument = musicalInstrumentService.findById(id);
        return instrument.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<MusicalInstrument> addInstrument(@RequestBody MusicalInstrument instrument) {
        MusicalInstrument savedInstrument = musicalInstrumentService.saveInstrument(instrument);
        return ResponseEntity.ok(savedInstrument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MusicalInstrument> updateInstrument(@PathVariable Long id, @RequestBody MusicalInstrument updatedInstrument) {
        Optional<MusicalInstrument> existingInstrumentOpt = musicalInstrumentService.findById(id);

        if (existingInstrumentOpt.isPresent()) {
            MusicalInstrument existingInstrument = existingInstrumentOpt.get();
            existingInstrument.setInstrument(updatedInstrument.getInstrument());
            existingInstrument.setDescription(updatedInstrument.getDescription());

            MusicalInstrument savedInstrument = musicalInstrumentService.saveInstrument(existingInstrument);
            return ResponseEntity.ok(savedInstrument);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstrument(@PathVariable Long id) {
        boolean deleted = musicalInstrumentService.deleteInstrument(id);
        return deleted ? ResponseEntity.ok("Инструмент удалён") : ResponseEntity.notFound().build();
    }
}
