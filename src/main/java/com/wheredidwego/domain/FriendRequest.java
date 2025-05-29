package com.wheredidwego.domain;

import com.wheredidwego.enumerate.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @CreationTimestamp
    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = RequestStatus.PENDING;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }
}
