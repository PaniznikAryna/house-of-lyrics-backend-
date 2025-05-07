package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Test;
import com.houseoflyrics.backend.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public List<Test> findAllByLessonId(Long lessonId) {
        return testRepository.findAllByLessonId(lessonId);
    }

    public Optional<Test> findById(Long id){
        return testRepository.findById(id);
    }

    public Test saveTest(Test test){
        return testRepository.save(test);
    }

    public boolean deleteTest(Long id) {
        if (testRepository.existsById(id)){
            testRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
