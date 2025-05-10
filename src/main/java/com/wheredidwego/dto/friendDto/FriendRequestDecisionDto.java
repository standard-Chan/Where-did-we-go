package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendRequestDecisionDto {
    private Long friendRequestId;
    private RequestStatus status;
}
