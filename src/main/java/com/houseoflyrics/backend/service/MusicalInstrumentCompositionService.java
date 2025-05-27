package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.MusicalInstrumentComposition;
import com.houseoflyrics.backend.repository.MusicalInstrumentCompositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicalInstrumentCompositionService {
    private final MusicalInstrumentCompositionRepository repository;

    public MusicalInstrumentCompositionService(MusicalInstrumentCompositionRepository repository) {
        this.repository = repository;
    }

    public List<MusicalInstrumentComposition> findAll() {
        return repository.findAll();
    }

    public Optional<MusicalInstrumentComposition> findById(Long id) {
        return repository.findById(id);
    }

    public List<MusicalInstrumentComposition> findByMusicalInstrument(Long musicalInstrumentId) {
        return repository.findByMusicalInstrument_Id(musicalInstrumentId);
    }

    public List<MusicalInstrumentComposition> findByComposition(Long compositionId) {
        return repository.findByComposition_Id(compositionId);
    }

    public MusicalInstrumentComposition saveComposition(MusicalInstrumentComposition composition) {
        return repository.save(composition);
    }

    public boolean deleteComposition(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
