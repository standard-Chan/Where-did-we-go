# 친구 요청 기능 (FriendRequest)

## 1. 개요

사용자 간 친구 요청을 보내고, 받은 요청/보낸 요청 목록을 조회하며, 수락 또는 거절로 상태를 변경할 수 있습니다. 친구 요청이 수락되면 양방향 `Friend` 엔티티가 생성됩니다.

---

## 2. 엔드포인트 목록

| 메서드   | URL                                | 설명                   |
| ----- | ---------------------------------- | -------------------- |
| POST  | `/api/v1/friends-request`          | 친구 요청 보내기            |
| GET   | `/api/v1/friends-request/received` | 받은 친구 요청 목록 조회       |
| GET   | `/api/v1/friends-request/sent`     | 보낸 친구 요청 목록 조회       |
| PATCH | `/api/v1/friends-request`          | 친구 요청 수락 또는 거절 상태 변경 |

---

## 3. 상세 API 설명

### 3-1. 친구 요청 보내기

* **URL**: `POST /api/v1/friends-request`
* **요청 Body**:

```json
{
  "friendEmail": "요청할_친구_이메일@google.com"
}
```

* **응답 예시**:

```json
{
  "friendRequestId": 1001,
  "receiverNickname": "홍길동",
  "receiverEmail": "hong@example.com",
  "requestedAt": "2025-05-09 15:29:55",
  "status": "PENDING"
}
```

* **참고**:

    * 상태 코드 201 Created
    * 이미 친구이거나 요청을 중복으로 보낼 경우 400 Bad Request 발생

---

### 3-2. 받은 친구 요청 목록 조회

* **URL**: `GET /api/v1/friends-request/received`
* **설명**: 로그인 사용자가 받은 친구 요청 목록을 조회합니다.
* **응답 예시**:

```json
[
  {
    "friendRequestId": 1001,
    "senderNickname": "jeong",
    "senderEmail": "jeong@example.com",
    "requestedAt": "2025-05-08T13:30:00",
    "status": "PENDING"
  },
  ...
]
```

---

### 3-3. 보낸 친구 요청 목록 조회

* **URL**: `GET /api/v1/friends-request/sent`
* **설명**: 로그인 사용자가 보낸 친구 요청 목록을 조회합니다.
* **응답 예시**:

```json
[
  {
    "friendRequestId": 1,
    "receiverNickname": "홍길동",
    "receiverEmail": "hong@example.com",
    "requestedAt": "2025-05-09 15:29:55",
    "status": "PENDING"
  },
  ...
]
```

---

### 3-4. 친구 요청 수락 / 거절

* **URL**: `PATCH /api/v1/friends-request`
* **요청 Body**:

```json
{
  "friendRequestId": 1,
  "status": "ACCEPTED"
}
```

* **응답 예시**:

```json
{
  "receiverNickname": "홍길동",
  "receiverEmail": "hong@example.com",
  "senderNickname": "이산",
  "senderEmail": "2san@example.com",
  "respondedAt": "2025-05-09T15:29:55",
  "status": "ACCEPTED"
}
```

* **참고**:

    * 상태는 `ACCEPTED`, `REJECTED` 중 하나여야 합니다.
    * 수락 시 양방향 `Friend` 엔티티가 생성됩니다.
    * 잘못된 상태 값 또는 중복 수락 시 400 Bad Request 발생

---

## 4. 응답 코드 정리

| 코드  | 설명                           |
| --- | ---------------------------- |
| 200 | 요청 성공                        |
| 201 | 친구 요청 등록 성공                  |
| 400 | 잘못된 요청 (중복 수락, 잘못된 status 등) |
| 401 | 인증 실패                        |
| 404 | 사용자 혹은 요청 정보 없음              |

---
