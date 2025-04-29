package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.UploadedComposition;
import com.houseoflyrics.backend.entity.Users;
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

    public List<UploadedComposition> findAllByUserId(Long userId) {
        return uploadedCompositionRepository.findAllByUserId(userId);
    }

    public Optional<UploadedComposition> findByUserAndTitle(Users user, String title) {
        return uploadedCompositionRepository.findByUserAndTitle(user, title);
    }

    public Optional<UploadedComposition> findById(Long id) {
        return uploadedCompositionRepository.findById(id);
    }

    public UploadedComposition saveUploadedComposition(UploadedComposition composition) {
        return uploadedCompositionRepository.save(composition);
    }

    public boolean deleteUploadedComposition(Long id) {
        if (uploadedCompositionRepository.existsById(id)) {
            uploadedCompositionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
