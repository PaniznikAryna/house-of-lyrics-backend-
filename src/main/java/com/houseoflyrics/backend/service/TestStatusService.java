package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Test;
import com.houseoflyrics.backend.entity.TestStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.TestStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestStatusService {
    private final TestStatusRepository testStatusRepository;
    private final UserService userService;

    public TestStatusService(TestStatusRepository testStatusRepository, UserService userService) {
        this.testStatusRepository = testStatusRepository;
        this.userService = userService;
    }

    public List<TestStatus> findAll() {
        return testStatusRepository.findAll();
    }

    public Optional<TestStatus> findById(Long id) {
        return testStatusRepository.findById(id);
    }

    public List<TestStatus> findByUserId(Long userId) {
        return testStatusRepository.findByUser_Id(userId);
    }

    public long countByUserAndStatus(Users user, TestStatus.TestStatusEnum status) {
        return testStatusRepository.countByUserAndStatus(user, status);
    }

    public TestStatus saveTestStatus(TestStatus testStatus) {
        return testStatusRepository.save(testStatus);
    }

    public boolean deleteTestStatus(Long id) {
        if (testStatusRepository.existsById(id)) {
            testStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public void assignDefaultStatusToAllUsers(Test test) {
        List<Users> allUsers = userService.findAll();
        for (Users user : allUsers) {
            TestStatus defaultStatus = new TestStatus(
                    test,
                    user,
                    TestStatus.TestStatusEnum.НЕ_НАЧАТО,
                    0.0
            );
            testStatusRepository.save(defaultStatus);
        }
    }
}
