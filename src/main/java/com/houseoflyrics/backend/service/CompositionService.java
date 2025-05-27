package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Composition;
import com.houseoflyrics.backend.repository.CompositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompositionService {
    private final CompositionRepository compositionRepository;

    public CompositionService(CompositionRepository compositionRepository) {
        this.compositionRepository = compositionRepository;
    }

    public List<Composition> findAll() {
        return compositionRepository.findAll();
    }

    public Optional<Composition> findById(Long id) {
        return compositionRepository.findById(id);
    }

    public List<Composition> findByTitle(String title) {
        return compositionRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Composition> findByComposer(Long composerId) {
        return compositionRepository.findByComposer_Id(composerId);
    }

    public List<Composition> findByTonality(String tonality) {
        return compositionRepository.findByTonalityIgnoreCase(tonality);
    }

    public List<Composition> findByDifficultyLevel(String difficultyLevel) {
        return compositionRepository.findByDifficultyLevelIgnoreCase(difficultyLevel);
    }

    public Composition saveComposition(Composition composition) {
        return compositionRepository.save(composition);
    }

    public boolean deleteComposition(Long id) {
        if (compositionRepository.existsById(id)) {
            compositionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
