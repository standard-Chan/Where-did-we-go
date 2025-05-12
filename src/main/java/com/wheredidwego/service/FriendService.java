package com.wheredidwego.service;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import com.wheredidwego.exception.FriendException;
import com.wheredidwego.repository.FriendRepository;
import com.wheredidwego.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final UserService userService;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 해당 유저의 친구 목록 조회
    public Set<Friend> getFriendsByUser(User user) {
        return user.getFriends();
    }

    // 해당 유저의 친구 삭제
    public void deleteFriend(User user, User friendUser) {
        // 유효성 검사
        // 나 -> 친구
        Friend friend = user.getFriends()
                .stream().filter((f) -> {
                    System.out.println("user - friends : " + f.getFriend().getId());
                    return f.getFriend().getId() == friendUser.getId();})
                .findFirst().orElseThrow(() -> new EntityNotFoundException("해당 친구관계가 존재하지 않습니다."));
        // 친구 -> 나
        Friend friendOf = friendUser.getFriends()
                .stream().filter((f) -> {
                    System.out.println("friend - user : " + f.getFriend().getId());
                        return f.getFriend().getId() == user.getId();})
                .findFirst().orElseThrow(() -> new EntityNotFoundException("해당 친구관계가 존재하지 않습니다."));


        if (friend.getUser() != user) {
            throw new FriendException("삭제 권한이 없습니다.");
        }

        // 단방향의 2개 친구관계 모두 제거
        // 부모 컬렉션에서 제거 (User의 Set<Friend> 가 부모)
        user.getFriends().remove(friend);
        // 자식 엔티티에서 부모 참조를 제거
        friend.setNull();

        friendUser.getFriends().remove(friendOf);
        friendOf.setNull();

        friendRepository.delete(friend);
        friendRepository.delete(friendOf);
    }

}
