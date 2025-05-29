package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.enumerate.FriendAccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendUpdateResponse {
    private Long friendEntityId;
    private FriendAccessLevel accessLevel;
    private String description;

    public FriendUpdateResponse(Friend friend) {
        this.friendEntityId = friend.getId();
        this.accessLevel = friend.getAccessLevel();
        this.description = friend.getDescription();
    }
}