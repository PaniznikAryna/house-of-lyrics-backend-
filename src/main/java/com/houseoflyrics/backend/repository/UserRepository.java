package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail); // поиск пользователя по email
}
