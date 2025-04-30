# 사진/활동 기록 (PhotoEntry)

## 1. 개요
사용자가 지도 기반으로 사진과 활동 기록을 업로드하는 기능입니다.  
AWS S3에 사진을 저장하고, 서버에는 사진 메타데이터(설명, 촬영일, 좌표 등)를 저장합니다.

---

## 2. 엔드포인트

| 메서드 | URL                                         | 설명 |
|:---|:--------------------------------------------|:---|
| GET | `/api/photo-entries/me`                     | 내 사진 목록 전체 조회 |
| POST | `/api/photo-entries`                        | 사진 업로드 및 활동 등록 |
| GET | `/api/photo-entries/:id`                    | id로 내 사진/활동 상세 조회 |
| DELETE | `/api/photo-entries/:id`                    | 내 사진/활동 삭제 |
| GET | `/api/s3/presignedUrl`                      | S3 Presigned URL + filename 발급 |
| GET (구현 예정) | `/api/photo-entries/search?lat=...&lng=...` | 자포로 내 사진/활동 조회 |

---

## 3. 사진 등록 전체 프로세스 (Front 처리)

1. **GET** `/api/s3/presignedUrl`
    - AWS S3로 업로드할 임시 URL(경로)가 발급됩니다.
    - 사진 filename 또한 같이 발급.

2. **PUT** `presignedUrl`
    - 발급된 URL에 지정된 사진 파일을 업로드
    - 200 OK 응답 결과 확인

3. **POST** `/api/photo-entries`
    - 1    - 1번에서 받은 filename과 사진 정보(설명, 좌표 등) 전달

4. **GET** `/api/photo-entries/:id`
    - 저장된 사진/활동 상세 조회

---

## 4. 상세 API 설명

### 4-1. S3 Presigned URL 발급

- **URL**: `GET /api/s3/presignedUrl`
- **응답 예시**
    ```json
    {
      "presignedUrl": "https://bucket.s3.amazonaws.com/user/photoEntry/{filename}?...",
      "filename": "{생성된_파일명}"
    }
    ```
- **참고**
    - URL은 일정 시간 후 만료됩니다.

---

### 4-2. 사진 업로드 및 활동 등록

- **URL**: `POST /api/photo-entries`
- **요청 Body**
    ```json
    {
      "filename": "{생성된_파일명}",
      "description": "설명",
      "takenAt": "2025-04-26",
      "lat": 37.5665,
      "lng": 126.9780
    }
    ```
- **응답**:
    ```text
    (201 Created)
    ```
- **참고**
    - 파일명은 4-1에서 response로 받은 filename을 사용해야합니다.
---

### 4-3. 내 사진 목록 조회

- **URL**: `GET /api/photo-entries/me`
- **응답 예시**:
    ```json
    [
      {
        "id": 1,
        "photoUrl": "https://signed-download-url",
        "description": "여행 사진",
        "takenAt": "2025-04-26",
        "lat": 37.5665,
        "lng": 126.9780,
        "province": "서울특별시",
        "district": "중구",
        "subdistrict": "명동"
      }
    ]
    ```

---

### 4-4. 내 사진/활동 단일 조회

- **URL**: `GET /api/photo-entries/:id`
- **응답 예시**:
    ```json
    {
      "id": 1,
      "photoUrl": "https://signed-download-url",
      "description": "여행 사진",
      "takenAt": "2025-04-26",
      "lat": 37.5665,
      "lng": 126.9780,
      "province": "서울특별시",
      "district": "중구",
      "subdistrict": "명동"
    }
    ```
---

### 4-5. 사진/활동 수정

- **URL**: `PUT /api/photo-entries`
- **요청 Body**
    ```json
    {
      "id": "{수정할 photo entry ID}",
      "description": "설명",
      "takenAt": "2025-04-26",
      "lat": 37.5665,
      "lng": 126.9780
    }
    ```
- **응답**:
    ```text
    (200 Created)
    ```

---

### 4-6. 내 사진/활동 삭제

- **URL**: `DELETE /api/photo-entries/:id`
- **응답**
    ```text
    삭제되었습니다.
    ```
- **참고**
    - Bad request 
      - 본인 소유가 아닐 경우
      - 해당 데이터가 존재하지 않을 경우

---

### 4-7. 자포 기반 사진/활동 조회 (구현 예정)

- **URL**: `GET /api/photo-entries/search?lat=...&lng=...`
- **설명**: 주어진 자포의 근처에 담겨진 사진/활동 기록 조회
- **상태**: 구현 예정

---

## 5. 응답 코드 정보

| 코드 | 설명                |
|:---|:------------------|
| 200 | 요청 성공             |
| 201 | 등록 성공 (사진/활동 등록)  |
| 400 | 잘못된 요청 (누락된 필드 등) |
| 401 | 인증 실패             |
| 404 | 리소스를 찾을 수 없음      |

---
