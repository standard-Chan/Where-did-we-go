# 회원가입/로그인

## 1. 개요

회원 가입 및 로그인 기능을 제공합니다.

로그인 시 JWT 토큰을 발급하여 쿠키에 저장하고, OAuth2 로그인도 지원합니다.

## 2. 엔드포인트 목록

| 메서드 | URL | 설명 |
| --- | --- | --- |
| POST | `/auth/signup` | 회원가입 |
| POST | `/auth/login` | 일반 로그인 (JWT 발급) |
| GET | `/oauth2/authorization/{provider}` | OAuth2 로그인 요청 |

## 3. API 설명

### 3-1. 회원가입

- **URL**: `POST /auth/signup`
- **요청 예시**:

    ```json
    {
      "email": "test@example.com",
      "password": "password",
      "nickname": "tester"
    }
    ```

- **응답** :

    ```
    회원가입에 성공하였습니다.
    ```

- **기타**:
    - 400 Bad Request 응답
        - 이메일 중복
        - 유효하지 않는 이메일 형식
        - 올바르지 않는 비밀번호 (문자 및 길이)
    - 비밀번호는 BCrypt로 암호화되어 저장

---

### 3-2. 일반 로그인

- **URL**: `POST /auth/login`
- **요청 예시**:

    ```json
    {
      "email": "test@example.com",
      "password": "password"
    }
    ```

- **응답 예시**:

    ```json
    {
      "token": "JWT_TOKEN_VALUE"
    }
    ```

    - **Set-Cookie**: `access_token=JWT_TOKEN_VALUE`
- **기타**:
    - 로그인 실패 시 401 Unauthorized 반환
    - JWT는 서버에서 발급되며 쿠키에 자동 저장

---

### 3-3. OAuth2 로그인 (ex: 구글 로그인)  (로그인 화면 구현 필요)

- **URL**: `GET /oauth2/authorization/google`
- **동작**:
    - Google 로그인 완료 시 JWT 토큰을 발급합니다.
    - `http://localhost:8080/success?token=JWT_TOKEN` 으로 리다이렉트됩니다.
    - 동시에 쿠키에도 `access_token` 이 저장됩니다.
- **기타**:
    - 별도로 로그인 완료 화면을 준비하거나 리다이렉트 처리 필요

---

## 4. 응답 코드 정리

| 코드 | 설명 |
| --- | --- |
| 200 | 요청 성공 |
| 400 | 회원가입 실패 (이메일 중복 등) |
| 401 | 로그인 실패 (잘못된 비밀번호 등) |

---

## 5. 참고

- 로그인 성공 시 JWT 토큰은 응답 본문 + 쿠키로 이중 전달됩니다.
- 모든 `/api/**` 요청은 인증(JWT)이 필요합니다.
- `/auth/**`, `/oauth2/**`, `/h2-console/**`는 인증 없이 접근 가능합니다.