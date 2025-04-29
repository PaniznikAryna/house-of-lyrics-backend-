package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Statistics;
import com.houseoflyrics.backend.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<Statistics> findAll() {
        return statisticsRepository.findAll();
    }


    public Optional<Statistics> findById(Long id) {
        return statisticsRepository.findById(id);
    }

    public Statistics saveStatistics(Statistics statistics) {
        return statisticsRepository.save(statistics);
    }

    public boolean deleteStatistics(Long id) {
        if (statisticsRepository.existsById(id)) {
            statisticsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
