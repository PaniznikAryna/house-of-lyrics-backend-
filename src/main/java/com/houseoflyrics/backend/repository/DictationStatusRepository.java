package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.DictationStatus;
import com.houseoflyrics.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictationStatusRepository extends JpaRepository<DictationStatus, Long> {
    List<DictationStatus> findByUser_Id(Long userId);
    long countByUserAndStatus(Users user, DictationStatus.DictationStatusEnum status);
}
