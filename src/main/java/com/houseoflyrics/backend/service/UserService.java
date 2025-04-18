package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.User;
import com.houseoflyrics.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByMail(String mail) {
        return userRepository.findByMail(mail);
    }

    public boolean authenticate(String mail, String rawPassword) {
        Optional<User> userOpt = userRepository.findByMail(mail);
        return userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }

    public User saveUser(User user){
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
