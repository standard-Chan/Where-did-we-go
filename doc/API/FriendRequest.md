# 친구 요청 API (FriendRequest)

## 1. 개요
사용자 간 친구 요청, 요청 목록 조회, 수락/거절 등의 상태 변경을 처리하는 기능입니다.  
친구 요청 수락 시 Friend 엔티티가 단방향으로 2개 생성됩니다.

---

## 2. 엔드포인트

| 메서드 | URL                          | 설명                          |
|:------:|:-----------------------------|:------------------------------|
| POST   | `/api/v1/friends-request`    | 친구 요청 보내기                 |
| GET    | `/api/v1/friends-request`    | 나에게 온 친구 요청 목록 조회       |
| PATCH  | `/api/v1/friends-request`    | 친구 요청 수락 또는 거절 상태 변경  |

---

## 3. 상세 API 설명

### 3-1. 친구 요청 보내기

- **URL**: `POST /api/v1/friends-request`
- **요청 Body**:
```json
{
  "friendEmail": "요청할_친구_이메일@google.com" 
}
```
- **응답 예시**:
```json
{
  "requestId": 1001,
  "status": "PENDING",
  "requestedAt": "2025-05-08T13:30:00"
}
```
- **참고** 
    - 201 Created : 성공적으로 응답 시  FriendRequest가 생성됩니다.
    - BadRequest 
      - 친구 요청을 이미 전송 한 경우 
      - 이미 친구인 경우
      - 해당 유저가 존재하지 않는 경우
---

### 3-2. 친구 요청 목록 조회

- **URL**: `GET /api/v1/friends-request?type=`
- **설명**: 받은 친구 요청 / 보낸 친구 요청 검색 
- **요청 파라미터**:
    - `type`: 받은 요청 `RECEIVED`, 보낸요청 `SENT`
    - 예: `/api/v1/friends-request?type=SENT`

- **응답 예시**:
- /api/v1/friends-request?type=RECEIVED
```json
[
  {
    "friendRequestId": 1001,
    "senderNickname": "jeong",
    "senderEmail": "jeong@example.com",
    "requestedAt": "2025-05-08T13:30:00",
    "status": "PENDING"
  }, 
  {
    ...
  }
]
```
- /api/v1/friends-request?type=SENT
```json
[
    {
        "friendRequestId": 1,
        "receiverNickname": "홍길동",
        "receiverEmail": "hong@example.com",
        "requestedAt": "2025-05-09 15:29:55",
        "status": "PENDING"
    }
]
```
- **기타**
---

### 3-3. 친구 요청 수락 / 거절

- **URL**: `PATCH /api/v1/friends-request`
- **요청 Body**:
```json
{
  "friendRequestId": 1,
  "status": "ACCEPTED" 
}
```
- **응답 예시**:
```json
{
  "receiverNickname": "홍길동",
  "receiverEmail": "hong@example.com",
  "senderNickname": "이산",
  "senderEmail": "2san@example.com",
  "respondedAt": "2025-05-09 15:29:55",
  "status": "ACCEPTED"
}
```

- **참고**:
    - `ACCEPTED`일 경우 Friend 엔티티가 자동 생성됩니다.
    - Bad Request(400)
      - 잘못된 JSON 응답
      - 친구 중복 처리 시
      - 해당 요청을 이미 보냈을 경우(요청을 받는 사람이 PENDING 상태인 경우)
---

## 4. 응답 코드 정보

| 코드 | 설명                          |
|:----:|:------------------------------|
| 200  | 요청 성공                        |
| 201  | 친구 요청 등록 성공                 |
| 400  | 잘못된 요청 (중복 수락, 잘못된 status 등) |