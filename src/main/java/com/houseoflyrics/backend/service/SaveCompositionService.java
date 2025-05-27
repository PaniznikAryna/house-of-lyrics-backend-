package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.SaveComposition;
import com.houseoflyrics.backend.repository.SaveCompositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaveCompositionService {
    private final SaveCompositionRepository saveCompositionRepository;

    public SaveCompositionService(SaveCompositionRepository saveCompositionRepository) {
        this.saveCompositionRepository = saveCompositionRepository;
    }

    public List<SaveComposition> findAll() {
        return saveCompositionRepository.findAll();
    }

    public Optional<SaveComposition> findById(Long id) {
        return saveCompositionRepository.findById(id);
    }

    public List<SaveComposition> findByUser(Long userId) {
        return saveCompositionRepository.findByUser_Id(userId);
    }

    public List<SaveComposition> findByFolder(String folder) {
        return saveCompositionRepository.findByFolderIgnoreCase(folder);
    }

    public SaveComposition saveComposition(SaveComposition saveComposition) {
        return saveCompositionRepository.save(saveComposition);
    }

    public boolean deleteComposition(Long id) {
        if (saveCompositionRepository.existsById(id)) {
            saveCompositionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
