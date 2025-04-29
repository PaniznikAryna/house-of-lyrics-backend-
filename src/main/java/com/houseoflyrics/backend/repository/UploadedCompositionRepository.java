package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.UploadedComposition;
import com.houseoflyrics.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadedCompositionRepository extends JpaRepository<UploadedComposition, Long> {
    List<UploadedComposition> findAllByUserId(Long userId);
    Optional<UploadedComposition> findByUserAndTitle(Users user, String title);
}
