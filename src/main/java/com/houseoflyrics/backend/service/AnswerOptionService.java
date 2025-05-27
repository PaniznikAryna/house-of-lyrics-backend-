package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.AnswerOption;
import com.houseoflyrics.backend.repository.AnswerOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerOptionService {
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }

    public List<AnswerOption> findAll() {
        return answerOptionRepository.findAll();
    }

    public Optional<AnswerOption> findById(Long id) {
        return answerOptionRepository.findById(id);
    }

    public List<AnswerOption> findByTaskId(Long taskId) {
        return answerOptionRepository.findByTask_Id(taskId);
    }

    public List<AnswerOption> findCorrectByTaskId(Long taskId) {
        return answerOptionRepository.findByTask_IdAndCorrectTrue(taskId);
    }

    public AnswerOption saveAnswerOption(AnswerOption answerOption) {
        return answerOptionRepository.save(answerOption);
    }

    public boolean deleteAnswerOption(Long id) {
        if (answerOptionRepository.existsById(id)) {
            answerOptionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
