package com.wheredidwego.service;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import com.wheredidwego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserService userService;
    private final UserRepository userRepository;

    public Set<Friend> getFriendsByUser(User user) {
        return user.getFriends();
    }
}
