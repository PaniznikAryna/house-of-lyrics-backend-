package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Composer;
import com.houseoflyrics.backend.repository.ComposerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComposerService {
    private final ComposerRepository composerRepository;

    public ComposerService(ComposerRepository composerRepository) {
        this.composerRepository = composerRepository;
    }

    public List<Composer> findAll() {
        return composerRepository.findAll();
    }

    public Optional<Composer> findByName(String name) {
        return composerRepository.findByName(name);
    }

    public Optional<Composer> findById(Long id) {
        return composerRepository.findById(id);
    }

    public Composer saveComposer(Composer composer) {
        return composerRepository.save(composer);
    }

    public boolean deleteComposer(Long id) {
        if (composerRepository.existsById(id)) {
            composerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
