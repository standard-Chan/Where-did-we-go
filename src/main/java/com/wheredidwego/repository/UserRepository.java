package com.wheredidwego.repository;

import com.wheredidwego.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByNickname(String nickname);

    Boolean existsUserByEmail(String email);
}
