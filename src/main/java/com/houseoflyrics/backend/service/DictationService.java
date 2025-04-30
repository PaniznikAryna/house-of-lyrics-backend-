package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Dictation;
import com.houseoflyrics.backend.repository.DictationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DictationService {
    private final DictationRepository dictationRepository;

    public DictationService(DictationRepository dictationRepository) {
        this.dictationRepository = dictationRepository;
    }

    public List<Dictation> findAll() {
        return dictationRepository.findAll();
    }

    public Optional<Dictation> findById(Long id){
        return dictationRepository.findById(id);
    }

    public Dictation saveDictation(Dictation dictation){
        return dictationRepository.save(dictation);
    }

    public boolean deleteDictation(Long id) {
        if (dictationRepository.existsById(id)){
            dictationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
