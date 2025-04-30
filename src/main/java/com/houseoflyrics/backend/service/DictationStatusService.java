package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Dictation;
import com.houseoflyrics.backend.entity.DictationStatus;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.DictationStatusRepository;
import com.houseoflyrics.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DictationStatusService {
    private final DictationStatusRepository dictationStatusRepository;
    private final UserService userService;

    public DictationStatusService(DictationStatusRepository dictationStatusRepository, UserService userService) {
        this.dictationStatusRepository = dictationStatusRepository;
        this.userService = userService;
    }

    public List<DictationStatus> findAll() {
        return dictationStatusRepository.findAll();
    }

    public Optional<DictationStatus> findById(Long id) {
        return dictationStatusRepository.findById(id);
    }

    public List<DictationStatus> findByUserId(Long userId) {
        return dictationStatusRepository.findByUser_Id(userId);
    }

    public long countByUserAndStatus(Users user, DictationStatus.DictationStatusEnum status) {
        return dictationStatusRepository.countByUserAndStatus(user, status);
    }

    public DictationStatus saveDictationStatus(DictationStatus dictationStatus) {
        return dictationStatusRepository.save(dictationStatus);
    }

    public boolean deleteDictationStatus(Long id) {
        if (dictationStatusRepository.existsById(id)) {
            dictationStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public void assignDefaultStatusToAllUsers(Dictation dictation) {
        List<Users> allUsers = userService.findAll();
        for (Users user : allUsers) {
            DictationStatus defaultStatus = new DictationStatus(
                    dictation,
                    user,
                    DictationStatus.DictationStatusEnum.НЕ_НАЧАТО,
                    0.0
            );
            dictationStatusRepository.save(defaultStatus);
        }
    }
}
