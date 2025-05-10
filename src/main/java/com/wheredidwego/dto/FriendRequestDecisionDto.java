package com.wheredidwego.dto;

import com.wheredidwego.domain.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendRequestDecisionDto {
    private Long requestId;
    private RequestStatus status;
}
