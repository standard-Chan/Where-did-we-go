package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.FriendAccessLevel;
import com.wheredidwego.domain.User;
import lombok.Getter;

@Getter
public class FriendResponseDto {
    private Long friendId;
    private String friendEmail;
    private String friendNickname;
    private FriendAccessLevel accessLevel;

    public FriendResponseDto(Friend friend) {
        User friendUser = friend.getFriend();
        this.friendId = friendUser.getId();
        this.friendEmail = friendUser.getEmail();
        this.friendNickname = friendUser.getNickname();
        this.accessLevel = friend.getAccessLevel();
    }
}
