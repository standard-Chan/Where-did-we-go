package com.wheredidwego.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wheredidwego.domain.FriendRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReceivedFriendRequestResponseDto {
    private Long friendRequestId;
    private String senderNickname;
    private String senderEmail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestedAt;

    public ReceivedFriendRequestResponseDto(FriendRequest friendRequest) {
        this.friendRequestId = friendRequest.getId();
        this.senderNickname = friendRequest.getSender().getNickname();
        this.senderEmail = friendRequest.getSender().getEmail();
        this.requestedAt = friendRequest.getRequestedAt();
    }

    public ReceivedFriendRequestResponseDto(Long friendRequestId,
                                            String senderEmail,
                                            String senderNickname,
                                            LocalDateTime requestedAt) {
        this.friendRequestId = friendRequestId;
        this.senderEmail = senderEmail;
        this.senderNickname = senderNickname;
        this.requestedAt = requestedAt;
    }
}
