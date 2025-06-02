# 친구 기능 (Friend)

## 1. 개요

친구 목록을 조회하고, 친구 정보(접근 권한, 설명)를 수정하거나 친구 관계를 삭제하는 기능입니다.

---

## 2. 엔드포인트 목록

| 메서드    | 경로                                 | 설명                   |
| :----- | :--------------------------------- | :------------------- |
| GET    | `/api/v1/friends`                  | 내 친구 목록 전체 조회        |
| PUT    | `/api/v1/friends/{friendEntityId}` | 친구 정보 수정 (접근 권한, 설명) |
| DELETE | `/api/v1/friends/{friendId}`       | 친구 삭제                |

---

## 3. 상세 API 설명

### 3-1. 내 친구 목록 조회

* **URL**: `GET /api/v1/friends`
* **설명**: 로그인한 사용자의 친구 목록을 모두 조회합니다.
* **응답 예시**:

```json
[
  {
    "friendEntityId": 12,
    "friendUserId": 9,
    "friendEmail": "friend@example.com",
    "friendNickname": "길동이",
    "description": "대학교 친구",
    "accessLevel": "FULL_ACCESS"
  },
  ...
]
```

---

### 3-2. 친구 정보 수정

* **URL**: `PUT /api/v1/friends/{friendEntityId}`
* **요청 Body**:

```json
{
  "accessLevel": "NONE",
  "description": "별로 안친한 친구"
}
```

* **응답 예시**:

```json
{
  "friendEntityId": 12,
  "accessLevel": "NONE",
  "description": "별로 안친한 친구"
}
```

* **accessLevel 값** 설명:

  * `NONE`: 권한 없음
  * `LOCATION_ONLY`: 위치 조회 가능
  * `VIEW_DETAIL`: 세부 정보 조회 가능
  * `FULL_ACCESS`: 모든 접근 가능

* **주의사항**:

  * `friendEntityId`는 DB 상 친구 테이블의 PK입니다.
  * 본인의 친구 데이터만 수정 가능합니다.

---

### 3-3. 친구 삭제

* **URL**: `DELETE /api/v1/friends/{friendId}`
* **설명**: 친구의 사용자 ID를 기반으로 친구 관계를 제거합니다 (양방향 모두 제거됨)
* **응답**:

```
삭제하였습니다.
```

* **주의사항**:

  * 해당 friendId는 친구의 `User.id`입니다.
  * 한쪽에서 삭제하면 상대편에서도 친구 관계가 제거됩니다.

---

## 4. 응답 코드 정리

| 코드  | 설명                            |
| :-- | :---------------------------- |
| 200 | 요청 성공                         |
| 400 | 잘못된 요청 또는 존재하지 않는 친구 ID/Email |
| 401 | 인증 실패                         |
| 403 | 접근 권한 없음                      |
| 404 | 친구 정보 없음                      |

---
