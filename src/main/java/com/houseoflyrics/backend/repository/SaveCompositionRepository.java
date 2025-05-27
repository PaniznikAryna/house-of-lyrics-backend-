package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.SaveComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SaveCompositionRepository extends JpaRepository<SaveComposition, Long> {
    List<SaveComposition> findByUser_Id(Long userId);
    List<SaveComposition> findByFolderIgnoreCase(String folder);
}
