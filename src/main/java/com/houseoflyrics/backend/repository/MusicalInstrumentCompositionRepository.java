package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.MusicalInstrumentComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MusicalInstrumentCompositionRepository extends JpaRepository<MusicalInstrumentComposition, Long> {
    List<MusicalInstrumentComposition> findByMusicalInstrument_Id(Long musicalInstrumentId);
    List<MusicalInstrumentComposition> findByComposition_Id(Long compositionId);
}
