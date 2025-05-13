package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.enumerate.FriendAccessLevel;
import com.wheredidwego.domain.User;
import lombok.Getter;

@Getter
public class FriendResponseDto {
    private Long friendEntityId;
    private Long friendUserId;
    private String friendEmail;
    private String friendNickname;
    private String discription;
    private FriendAccessLevel accessLevel;

    public FriendResponseDto(Friend friend) {
        this.friendEntityId = friend.getId();
        User friendUser = friend.getFriend();
        this.discription = friend.getDescription();
        this.friendUserId = friendUser.getId();
        this.friendEmail = friendUser.getEmail();
        this.friendNickname = friendUser.getNickname();
        this.accessLevel = friend.getAccessLevel();
    }
}
