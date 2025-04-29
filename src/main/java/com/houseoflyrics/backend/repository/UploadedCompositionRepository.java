package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.UploadedComposition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedCompositionRepository extends JpaRepository<UploadedComposition, Long> {
}
