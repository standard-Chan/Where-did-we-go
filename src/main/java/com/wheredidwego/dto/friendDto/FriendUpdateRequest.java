package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.enumerate.FriendAccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendUpdateRequest {
    private FriendAccessLevel accessLevel;
    private String description;

    public FriendUpdateRequest(Friend friend) {
        this.accessLevel = friend.getAccessLevel();
        this.description = friend.getDescription();
    }
}
