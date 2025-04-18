package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.MusicalInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MusicalInstrumentRepository extends JpaRepository<MusicalInstrument, Long> {
    Optional<MusicalInstrument> findByInstrument(String instrument);
}

