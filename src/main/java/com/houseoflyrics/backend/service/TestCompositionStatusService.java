package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.TestComposition;
import com.houseoflyrics.backend.entity.TestCompositionStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.TestCompositionStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestCompositionStatusService {

    private final TestCompositionStatusRepository repository;
    private final TestCompositionService testCompositionService;

    public TestCompositionStatusService(TestCompositionStatusRepository repository,
                                        TestCompositionService testCompositionService) {
        this.repository = repository;
        this.testCompositionService = testCompositionService;
    }

    public List<TestCompositionStatus> findAll() {
        return repository.findAll();
    }

    public Optional<TestCompositionStatus> findById(Long id) {
        return repository.findById(id);
    }

    public List<TestCompositionStatus> findByUser(Long userId) {
        return repository.findByUser_Id(userId);
    }

    public List<TestCompositionStatus> findNotPassed() {
        return repository.findByResultLessThan(60.0);
    }

    public List<TestCompositionStatus> findPassed() {
        return repository.findByResultGreaterThanEqual(60.0);
    }

    public TestCompositionStatus saveStatus(TestCompositionStatus status) {
        return repository.save(status);
    }

    public boolean deleteStatus(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<TestCompositionStatus> findByUserAndTestComposition(Users user, TestComposition testComposition) {
        return repository.findByTestComposition_IdAndUser_Id(testComposition.getId(), user.getId());
    }


    public void assignDefaultStatusToUser(Users user) {
        List<TestComposition> allCompositionTests = testCompositionService.findAll();
        for (TestComposition test : allCompositionTests) {
            Optional<TestCompositionStatus> optStatus = repository.findByTestComposition_IdAndUser_Id(test.getId(), user.getId());
            if (optStatus.isEmpty()) {
                TestCompositionStatus status = new TestCompositionStatus(test, user, 0.0);
                repository.save(status);
            }
        }
    }



    public void assignDefaultStatusToAllUsers(TestComposition testComposition, List<Users> users) {
        for (Users user : users) {
            Optional<TestCompositionStatus> optStatus = repository.findByTestComposition_IdAndUser_Id(testComposition.getId(), user.getId());
            if (optStatus.isEmpty()) {
                TestCompositionStatus status = new TestCompositionStatus(testComposition, user, 0.0);
                repository.save(status);
            }
        }
    }
}
