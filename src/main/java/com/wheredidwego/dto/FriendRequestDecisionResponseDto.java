package com.wheredidwego.dto;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.RequestStatus;
import com.wheredidwego.domain.User;
import lombok.Getter;


@Getter
public class FriendRequestDecisionResponseDto {
    private User receiver;
    private User sender;
    private RequestStatus status;

    public FriendRequestDecisionResponseDto(FriendRequest friendRequest) {
        this.sender = friendRequest.getSender();
        this.receiver = friendRequest.getReceiver();
        this.status = friendRequest.getStatus();
    }
}
