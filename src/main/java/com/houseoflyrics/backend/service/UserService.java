package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.MusicalInstrument;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.MusicalInstrumentRepository;
import com.houseoflyrics.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MusicalInstrumentRepository musicalInstrumentRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       MusicalInstrumentRepository musicalInstrumentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.musicalInstrumentRepository = musicalInstrumentRepository;
    }

    public Users registerUser(Users user) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        MusicalInstrument instrumentFromDb = musicalInstrumentRepository.findById(
                        user.getMusicalInstrument().getId())
                .orElseThrow(() -> new IllegalArgumentException("Инструмент с указанным id не найден"));

        user.setMusicalInstrument(instrumentFromDb);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Optional<Users> findById(Long id){
        return userRepository.findById(id);
    }


    public Optional<Users> findByMail(String mail) {
        return userRepository.findByMail(mail);
    }

    public boolean authenticate(String mail, String rawPassword) {
        Optional<Users> userOpt = userRepository.findByMail(mail);
        return userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }

    public Users saveUser(Users user){
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id){
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Users updateUser(Users currentUser, Users targetUser, Users updatedData) {
        if (currentUser.isAdmin() && !currentUser.getId().equals(targetUser.getId())) {
            targetUser.setAdmin(updatedData.isAdmin());
        }

        if (currentUser.getId().equals(targetUser.getId())) {
            targetUser.setNickname(updatedData.getNickname());
            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                targetUser.setPassword(passwordEncoder.encode(updatedData.getPassword()));
            }
            targetUser.setProfilePicture(updatedData.getProfilePicture());
        }
        return userRepository.save(targetUser);
    }

}
