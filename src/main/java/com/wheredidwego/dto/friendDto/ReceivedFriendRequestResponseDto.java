package com.wheredidwego.dto.friendDto;

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
    private LocalDateTime requestedAt;
    private String status;

    public ReceivedFriendRequestResponseDto(FriendRequest friendRequest) {
        this.friendRequestId = friendRequest.getId();
        this.senderNickname = friendRequest.getSender().getNickname();
        this.senderEmail = friendRequest.getSender().getEmail();
        this.requestedAt = friendRequest.getRequestedAt();
        this.status = friendRequest.getStatus().getDescription();
    }
}
