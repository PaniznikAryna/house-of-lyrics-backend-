package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Composition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {
    List<Composition> findByTitleContainingIgnoreCase(String title);
    List<Composition> findByComposer_Id(Long composerId);
    List<Composition> findByTonalityIgnoreCase(String tonality);
    List<Composition> findByDifficultyLevelIgnoreCase(String difficultyLevel);
}
