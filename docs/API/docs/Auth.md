# Auth API

## 1. 개요

회원 가입 및 로그인 기능을 제공합니다.

로그인 시 JWT 토큰을 발급하여 쿠키에 저장하고, 로그아웃 시 쿠키 삭제를 통해 인증을 해제합니다.

## 2. 엔드포인트 목록

| 메서드  | URL            | 설명               |
| ---- | -------------- | ---------------- |
| POST | `/auth/signup` | 회원가입             |
| POST | `/auth/login`  | 일반 로그인 (JWT 발급)  |
| GET  | `/auth/logout` | 로그아웃 (JWT 쿠키 제거) |

---

## 3. API 설명

### 3-1. 회원가입

* **URL**: `POST /auth/signup`
* **요청 예시**:

```json
{
  "email": "test@example.com",
  "password": "password",
  "nickname": "tester"
}
```

* **응답**:

```
회원가입에 성공하였습니다.
```

* **참고**:

  * 400 Bad Request 발생 가능

    * `ALREADY_REGISTERED_EMAIL`
    * `INVALID_EMAIL_FORMAT`
    * `INVALID_PASSWORD_LENGTH`
    * `INVALID_NICKNAME_FORMAT`
  * 비밀번호는 BCrypt로 암호화되어 저장됨

---

### 3-2. 일반 로그인

* **URL**: `POST /auth/login`
* **요청 예시**:

```json
{
  "email": "test@example.com",
  "password": "password"
}
```

* **응답 예시**:

```json
{
  "token": "JWT_TOKEN_VALUE"
}
```

* **Set-Cookie**: `access_token=JWT_TOKEN_VALUE`

* **참고**:

  * 401 Unauthorized: 로그인 실패 (잘못된 이메일/비밀번호)
  * JWT는 응답 본문 + 쿠키를 통해 전달됨

---

### 3-3. 로그아웃

* **URL**: `GET /auth/logout`
* **설명**: JWT 쿠키 삭제를 통해 로그아웃 처리
* **응답**:

```
로그아웃 완료
```

* **참고**:

  * `access_token` 쿠키 유효시간을 0으로 변경하여 만료시킴
  * 클라이언트 측에서 쿠키 자동 제거 유도

---

## 4. 응답 코드 정리

| 코드  | 설명                  |
| --- | ------------------- |
| 200 | 요청 성공               |
| 400 | 회원가입 실패 (이메일 중복 등)  |
| 401 | 로그인 실패 (잘못된 비밀번호 등) |

---

## 5. 인증 관련 정책 참고

* 로그인 성공 시 JWT 토큰은 **응답 본문 + 쿠키**로 이중 전달됩니다.
* 모든 `/api/**` 요청은 인증(JWT)이 필요합니다.
* 다음 경로는 인증 없이 접근 가능합니다:

  * `/auth/**`
  * `/oauth2/**`
  * `/h2-console/**`
