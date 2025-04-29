package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.UploadedComposition;
import com.houseoflyrics.backend.repository.UploadedCompositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UploadedCompositionService {
    private final UploadedCompositionRepository uploadedCompositionRepository;

    public UploadedCompositionService(UploadedCompositionRepository uploadedCompositionRepository) {
        this.uploadedCompositionRepository = uploadedCompositionRepository;
    }

    public List<UploadedComposition> findAll() {
        return uploadedCompositionRepository.findAll();
    }

//    public Optional<UploadedComposition> findByName(String name) {
//        return uploadedCompositionRepository.findByName(name);
//    }

    public Optional<UploadedComposition> findById(Long id) {
        return uploadedCompositionRepository.findById(id);
    }

    public UploadedComposition saveComposition(UploadedComposition composition) {
        return uploadedCompositionRepository.save(composition);
    }

    public boolean deleteComposition(Long id) {
        if (uploadedCompositionRepository.existsById(id)) {
            uploadedCompositionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
