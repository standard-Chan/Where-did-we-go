package com.wheredidwego.dto;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.RequestStatus;
import com.wheredidwego.domain.User;
import lombok.Getter;


@Getter
public class FriendRequestDecisionResponseDto {
    private String receiverNickname;
    private String receiverEmail;
    private String senderNickname;
    private String senderEmail;
    private RequestStatus status;

    public FriendRequestDecisionResponseDto(FriendRequest friendRequest) {
        this.senderNickname = friendRequest.getSender().getNickname();
        this.senderEmail = friendRequest.getSender().getEmail();
        this.receiverNickname = friendRequest.getReceiver().getNickname();
        this.receiverEmail = friendRequest.getReceiver().getEmail();
        this.status = friendRequest.getStatus();
    }
}
