package com.wheredidwego.repository;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByNickname(String nickname);
    Optional<User> findUserById(Long id);

    Boolean existsUserByEmail(String email);


}
