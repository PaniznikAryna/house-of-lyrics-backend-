package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Mark;
import com.houseoflyrics.backend.repository.MarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarkService {
    private final MarkRepository markRepository;

    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public List<Mark> findAll() {
        return markRepository.findAll();
    }

    public Optional<Mark> findById(Long id) {
        return markRepository.findById(id);
    }

    public List<Mark> findByUser(Long userId) {
        return markRepository.findByUser_Id(userId);
    }

    public List<Mark> findByComposition(Long compositionId) {
        return markRepository.findByComposition_Id(compositionId);
    }

    public Mark saveMark(Mark mark) {
        return markRepository.save(mark);
    }

    public boolean deleteMark(Long id) {
        if (markRepository.existsById(id)) {
            markRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
