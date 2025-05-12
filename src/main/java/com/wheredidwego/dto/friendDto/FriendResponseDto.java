package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.FriendAccessLevel;
import com.wheredidwego.domain.User;
import lombok.Getter;

@Getter
public class FriendResponseDto {
    private Long friendId;
    private Long friendUserId;
    private String friendEmail;
    private String friendNickname;
    private FriendAccessLevel accessLevel;

    public FriendResponseDto(Friend friend) {
        this.friendId = friend.getId();
        User friendUser = friend.getFriend();
        this.friendUserId = friendUser.getId();
        this.friendEmail = friendUser.getEmail();
        this.friendNickname = friendUser.getNickname();
        this.accessLevel = friend.getAccessLevel();
    }
}
