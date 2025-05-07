package com.houseoflyrics.backend.repository;


import com.houseoflyrics.backend.entity.TestStatus;
import com.houseoflyrics.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestStatusRepository extends JpaRepository<TestStatus, Long> {
    List<TestStatus> findByUser_Id(Long userId);
    long countByUserAndStatus(Users user, TestStatus.TestStatusEnum status);

}
