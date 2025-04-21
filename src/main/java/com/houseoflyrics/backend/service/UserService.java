package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users registerUser(Users user) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
}
