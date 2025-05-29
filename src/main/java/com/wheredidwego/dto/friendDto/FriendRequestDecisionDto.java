package com.wheredidwego.dto.friendDto;

import com.wheredidwego.enumerate.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendRequestDecisionDto {
    private Long friendRequestId;
    private RequestStatus status;
}
