package com.houseoflyrics.backend.repository;

import com.houseoflyrics.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByMail(String mail);
   // Optional<User> findByNickname(String nickname);

}

