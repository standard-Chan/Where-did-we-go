# 사진/활동 기록 (PhotoEntry)

## 1. 개요

지도 기반으로 사진과 활동 기록을 업로드하는 기능입니다.
AWS S3에 사진 파일을 업로드하고, 서버에는 메타데이터(설명, 촬영일, 좌표 등)를 저장합니다.

---

## 2. 엔드포인트 목록

| 메서드    | URL                                          | 설명                          |
| ------ | -------------------------------------------- | --------------------------- |
| GET    | `/api/v1/s3/presignedUrl`                    | S3 Presigned URL + 파일명 발급   |
| POST   | `/api/v1/photo-entries`                      | 사진 업로드 및 활동 등록              |
| GET    | `/api/v1/photo-entries/{id}`                 | 특정 사진/활동 상세 조회              |
| GET    | `/api/v1/photo-entries`                      | 특정 범위 내 내 사진/활동 조회          |
| GET    | `/api/v1/photo-entries/all`                  | 내 사진 전체 조회                  |
| PUT    | `/api/v1/photo-entries/{id}`                 | 사진/활동 정보 수정                 |
| DELETE | `/api/v1/photo-entries/{id}`                 | 사진/활동 삭제                    |
| GET    | `/api/v1/photo-entries/friend/{friendEmail}` | 친구의 사진/활동 목록 조회 (권한 기반 필터링) |
| GET    | `/api/v1/photo-entries/statistics/province`  | 지역별 사진 개수 통계                |

---

## 3. 사진 등록 전체 프로세스

1. **GET** `/api/v1/s3/presignedUrl`

    * AWS S3로 업로드할 임시 URL(경로)를 발급받습니다.
    * 함께 사용할 파일명(`filename`)도 함께 반환됩니다.

2. **PUT** `presignedUrl`

    * 발급된 presignedUrl에 해당하는 주소로 이미지 파일을 직접 업로드합니다.
    * 응답 상태코드 200이 반환되면 업로드 성공입니다.

3. **POST** `/api/v1/photo-entries`

    * 업로드한 이미지의 파일명과 사진 정보(설명, 날짜, 좌표 등)를 함께 전송하여 메타데이터 저장

4. **GET** `/api/v1/photo-entries/{id}`

    * 저장된 사진의 상세 정보를 조회할 수 있습니다.

---

## 4. 상세 API 설명

### 4-1. S3 Presigned URL 발급

* **URL**: `GET /api/v1/s3/presignedUrl`
* **설명**: 클라이언트가 S3에 직접 사진을 업로드할 수 있도록 사전 서명된 URL을 발급합니다.
* **응답 예시**:

```json
{
  "presignedUrl": "https://bucket.s3.amazonaws.com/user/photoEntry/{filename}?...",
  "filename": "1681234567890"
}
```

* **참고**: URL은 일정 시간이 지나면 만료됩니다.

---

### 4-2. 사진 업로드 및 활동 등록

* **URL**: `POST /api/v1/photo-entries`
* **요청 Body**:

```json
{
  "filename": "1681234567890",
  "description": "제주도 바다",
  "takenAt": "2025-04-26",
  "lat": 33.450701,
  "lng": 126.570667
}
```

* **응답 예시**:

```json
{
  "id": 1,
  "photoUrl": "https://signed-download-url",
  "description": "제주도 바다",
  "takenAt": "2025-04-26",
  "lat": 33.450701,
  "lng": 126.570667,
  "province": "제주특별자치도",
  "district": "제주시",
  "subdistrict": "구좌읍"
}
```

* **참고**:

    * `filename`은 4-1에서 받은 값을 사용해야 함

---

### 4-3. 사진/활동 단일 조회

* **URL**: `GET /api/v1/photo-entries/{id}`
* **응답 예시**: 위와 동일한 포맷의 상세 정보 반환

---

### 4-4. 범위 내 사진/활동 조회

* **URL**: `GET /api/v1/photo-entries`
* **요청 파라미터**:

    * `swLat`, `swLng`: 남서쪽 경계 (위도, 경도)
    * `neLat`, `neLng`: 북동쪽 경계 (위도, 경도)
* **응답 예시**:

```json
[
  {
    "id": 1,
    "photoUrl": "https://signed-download-url",
    "description": "제주 바다",
    "takenAt": "2025-04-26",
    "lat": 33.450701,
    "lng": 126.570667,
    "province": "제주특별자치도",
    "district": "제주시",
    "subdistrict": "구좌읍"
  }
]
```

---

### 4-5. 내 사진 전체 조회

* **URL**: `GET /api/v1/photo-entries/all`
* **응답**: 사용자의 전체 사진/활동 목록

---

### 4-6. 사진/활동 수정

* **URL**: `PUT /api/v1/photo-entries/{id}`
* **요청 Body**:

```json
{
  "description": "내용 수정",
  "takenAt": "2025-04-27",
  "lat": 33.450701,
  "lng": 126.570667
}
```

* **응답**: 수정된 사진의 정보

---

### 4-7. 사진/활동 삭제

* **URL**: `DELETE /api/v1/photo-entries/{id}`
* **응답**:

```
성공적으로 삭제되었습니다.
```

* **참고**:

    * 본인 소유가 아닌 경우 삭제 불가 (403 Forbidden)
    * 존재하지 않는 ID인 경우 404 Not Found

---

### 4-8. 친구의 사진/활동 조회

* **URL**: `GET /api/v1/photo-entries/friend/{friendEmail}`
* **요청 파라미터**:

    * `swLat`, `swLng`, `neLat`, `neLng`
* **응답 예시**:

    * `FULL_ACCESS`인 경우 전체 정보 제공
    * `ONLY_LOCATION_VIEW`인 경우 description/takenAt은 "false"로 마스킹됨

---

### 4-9. 지역별 사진 개수 통계

* **URL**: `GET /api/v1/photo-entries/statistics/province`
* **응답 예시**:

```json
[
  {
    "province": "서울특별시",
    "count": 12
  },
  {
    "province": "경기도",
    "count": 8
  }
]
```

* **참고**:

    * 로그인한 사용자의 데이터를 기준으로 집계됨

---

## 5. 응답 코드 정리

| 코드  | 설명                |
| --- | ----------------- |
| 200 | 요청 성공             |
| 201 | 등록 성공             |
| 400 | 유효하지 않은 입력        |
| 401 | 인증 실패             |
| 403 | 권한 없음             |
| 404 | 존재하지 않는 사진 또는 사용자 |

---
