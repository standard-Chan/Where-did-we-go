package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.enumerate.FriendAccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendUpdateDto {
    private Long friendEntityId;
    private FriendAccessLevel accessLevel;
    private String description;

    public FriendUpdateDto(Friend friend) {
        this.friendEntityId = friend.getId();
        this.accessLevel = friend.getAccessLevel();
        this.description = friend.getDescription();
    }
}
