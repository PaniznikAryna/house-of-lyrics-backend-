package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.MusicalInstrument;
import com.houseoflyrics.backend.repository.MusicalInstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MusicalInstrumentService {
    private final MusicalInstrumentRepository musicalInstrumentRepository;

    public MusicalInstrumentService(MusicalInstrumentRepository musicalInstrumentRepository) {
        this.musicalInstrumentRepository = musicalInstrumentRepository;
    }

   public Optional<MusicalInstrument> findByInstrument(String instrument){
        return musicalInstrumentRepository.findByInstrument(instrument);
   }

   public Optional<MusicalInstrument> findById(Long id){
        return musicalInstrumentRepository.findById(id);
   }

   public MusicalInstrument saveInstrument(MusicalInstrument instrument){
        return musicalInstrumentRepository.save(instrument);
   }

   public boolean deleteInstrument(Long id) {
        if (musicalInstrumentRepository.existsById(id)){
            musicalInstrumentRepository.deleteById(id);
            return true;
        }
        return false;
   }
}
