package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Composer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComposerRepository extends JpaRepository<Composer, Long> {
    Optional<Composer> findByName(String name);}
