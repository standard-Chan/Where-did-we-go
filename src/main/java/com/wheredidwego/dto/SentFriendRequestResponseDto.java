package com.wheredidwego.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wheredidwego.domain.FriendRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SentFriendRequestResponseDto {
    private Long friendRequestId;
    private String receiverNickname;
    private String receiverEmail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestedAt;
    private String status;

    public SentFriendRequestResponseDto(FriendRequest friendRequest) {
        this.friendRequestId = friendRequest.getId();
        this.receiverNickname = friendRequest.getReceiver().getNickname();
        this.receiverEmail = friendRequest.getReceiver().getEmail();
        this.requestedAt = friendRequest.getRequestedAt();
        this.status = friendRequest.getStatus().getDescription();
    }
}
