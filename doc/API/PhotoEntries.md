# 사진/활동 기록 (PhotoEntry)

## 1. 개요
사용자가 지도 기반으로 사진과 활동 기록을 업로드하는 기능입니다.  
AWS S3에 사진을 저장하고, 서버에는 사진 메타데이터(설명, 촬영일, 좌표 등)를 저장합니다.

---

## 2. 엔드포인트

| 메서드 | URL                          | 설명                             |
|:---|:-----------------------------|:-------------------------------|
| GET | `/api/photo-entries/all`     | 내 사진 목록 전체 조회                  |
| GET | `/api/photo-entries`         | 좌표 범위 내 사진 조회                  |
| POST | `/api/photo-entries`         | 사진 업로드 및 활동 등록                 |
| GET | `/api/photo-entries/:id`     | id로 내 사진/활동 상세 조회              |
| DELETE | `/api/photo-entries/:id`     | 내 사진/활동 삭제                     |
| GET | `/api/s3/presignedUrl`       | S3 Presigned URL + filename 발급 |
| GET | `/api/search/photo-entries?` | 정렬된 사진/활동 조회                   |

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

### 4-3. 좌표 범위 내의 사진 조회
- **URL**: `GET /api/photo-entries`
- 쿼리 파라미터
  - swLat :	없음	남서쪽 위도
  - swLng : 남서쪽 경도
  - neLat : 북동쪽 위도
  - neLng : 북동쪽 경도
- 
- **응답 예시**:
```json
[
  {
    "id": 1,
    "photoUrl": "https://s3.amazonaws.com/photos/123.jpg",
    "description": "제주 바다",
    "takenAt": "2024-05-06",
    "lat": 33.450701,
    "lng": 126.570667,
    "province": "제주특별자치도",
    "district": "제주시",
    "subdistrict": "구좌읍"
  },
  ...
]
```

### 4-4. 내 사진 전체 조회

- **URL**: `GET /api/photo-entries/all`
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

### 4-7. 내 사진 목록 정렬 및 페이징 조회

- **URL**: `URL: GET /api/search/photo-entries?sort=...&drection=...`
- **요청 Body**


- **요청 파라미터**

    - sort (기본값: takenAt): 정렬 기준 필드
      - sort 가능 값 (region, takenAt)
    - direction (기본값: desc): 오름차순 or 내림차순 
    - page (기본값: 0): 페이지 번호 (0부터 시작)
    - size (기본값: 10): 페이지당 항목 수

응답 예시:
GET /api/search/photo-entries?sort=takenAt&drection=asc&page=10&size=10
```
    {
        "content": [
            {
                "id": 1,
                "photoUrl": "https://signed-download-url",
                "description": "카페",
                "takenAt": "2024-05-06",
                "lat": 37.561,
                "lng": 126.982,
                "province": "서울특별시",
                "district": "중구",
                "subdistrict": "을지로동"
            },
            { ... }
        ],
            "pageable": {
            "pageNumber": 0,
            "pageSize": 10
        },
        "totalPages": 3,
        "totalElements": 25
    }
```

- ***참고***
    - 사용자 자신의 데이터에 대해서만 페이징 조회됩니다.
    - 정렬 필드는 유효한 컬럼 값이어야 합니다.


## 5. 응답 코드 정보

| 코드 | 설명                |
|:---|:------------------|
| 200 | 요청 성공             |
| 201 | 등록 성공 (사진/활동 등록)  |
| 400 | 잘못된 요청 (누락된 필드 등) |
| 401 | 인증 실패             |
| 404 | 리소스를 찾을 수 없음      |

---
