package com.wheredidwego.dto.friendDto;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.enumerate.RequestStatus;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class FriendRequestDecisionResponseDto {
    private String receiverNickname;
    private String receiverEmail;
    private String senderNickname;
    private String senderEmail;
    private LocalDateTime respondedAt;
    private RequestStatus status;

    public FriendRequestDecisionResponseDto(FriendRequest friendRequest) {
        this.senderNickname = friendRequest.getSender().getNickname();
        this.senderEmail = friendRequest.getSender().getEmail();
        this.receiverNickname = friendRequest.getReceiver().getNickname();
        this.receiverEmail = friendRequest.getReceiver().getEmail();
        this.respondedAt = friendRequest.getRespondedAt();
        this.status = friendRequest.getStatus();
    }
}
