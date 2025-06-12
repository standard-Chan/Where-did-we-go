package com.wheredidwego.service;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.enumerate.FriendAccessLevel;
import com.wheredidwego.domain.User;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.FriendException;
import com.wheredidwego.exception.PhotoEntryException;
import com.wheredidwego.repository.FriendRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final FriendRepository friendRepository;

    // 친구 조회
    public Friend getFriendByUserAndFriend(User user, User friend) {
        return friendRepository.findFriendByUserAndFriend(user, friend)
                .orElseThrow(() -> new FriendException(ErrorCode.FRIEND_NOT_FOUND));
    }

    public Friend getFriendById(Long friendEntityId) {
        return friendRepository.getFriendsById(friendEntityId)
                .orElseThrow(()-> new FriendException(ErrorCode.FRIEND_NOT_FOUND));
    }

    // 친구 관계 확인
    public Friend checkFriendShip(User user, User friend) {
        return friendRepository.findFriendByUserAndFriend(user, friend)
                .orElseThrow(()-> new FriendException(ErrorCode.IS_NOT_FRIEND));
    }

    public void checkPermission(User from, User to) {
        FriendAccessLevel accessLevel = getAccessLevel(from, to);
        if (FriendAccessLevel.NONE.equals(accessLevel)) {
            throw new PhotoEntryException(ErrorCode.NOT_PERMISSION_TO_VIEW);
        }
    }

    public Friend updateFriendInfo(User user, Friend friend, FriendAccessLevel accessLevel, String description) {
        checkFriendShip(user, friend.getFriend());

        if (!(friend.getAccessLevel() == accessLevel)) {
            friend.setAccessLevel(accessLevel);
        }
        if (!description.isEmpty() && !friend.getDescription().equals(description)){
            friend.setDescription(description);
        }

        return friendRepository.save(friend);
    }

    // 해당 유저의 친구 삭제
    public void deleteFriend(User user, User friendUser) {
        // 친구관계 edge
        Friend friend = findFriendEdge(user, friendUser);
        Friend friendOf = findFriendEdge(friendUser, user);

        // 친구가 아닌 경우
        if (friend.getUser() != user) {
            throw new FriendException(ErrorCode.NOT_PERMISSION_TO_DELETE);
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

    /**
     * User와 Friend의 Entity로 FriendEntity를 찾는 메서드
     * 방향성이 존재한다.
     * @param from User
     * @param to 친구 관계 대상자
     * @return frined Entity
     */
    private Friend findFriendEdge(User from, User to) {
        Friend friend = from.getFriends()
                .stream().filter((f) -> f.getFriend().getId().equals(to.getId()))
                .findFirst().orElseThrow(() -> new FriendException(ErrorCode.IS_NOT_FRIEND));

        return friend;
    }

    /**
     * from 유저의 to 유저에 대한 권한 레벨 반환 메서드
     * @param from 권한을 설정한 친구
     * @param to   권한을 지정 받은 유저
     * @return Access level
     */
    public FriendAccessLevel getAccessLevel(User from, User to) {
        Friend friendship = findFriendEdge(from, to);
        return friendship.getAccessLevel();
    }
}
