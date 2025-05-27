package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.TestComposition;
import com.houseoflyrics.backend.repository.TestCompositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestCompositionService {
    private final TestCompositionRepository repository;

    public TestCompositionService(TestCompositionRepository repository) {
        this.repository = repository;
    }

    public List<TestComposition> findAll() {
        return repository.findAll();
    }

    public Optional<TestComposition> findById(Long id) {
        return repository.findById(id);
    }

    public List<TestComposition> findByComposition(Long compositionId) {
        return repository.findByComposition_Id(compositionId);
    }

    public TestComposition saveTestComposition(TestComposition testComposition) {
        return repository.save(testComposition);
    }

    public List<TestComposition> saveAllTestCompositions(List<TestComposition> testCompositions) {
        return repository.saveAll(testCompositions);
    }

    public boolean deleteTestComposition(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
