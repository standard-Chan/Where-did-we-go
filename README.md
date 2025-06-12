## 💡 Background
Side Project로 개발한 위치 기반 사진 공유 SNS 입니다. 지도좌표에 자신의 사진 정보를 등록하고, 친구들과 공유하며 같이 업데이트해나갈 수 있습니다.

---
Spring/Java 과 MY SQL을 이용하여 백엔드 개발을 진행하였습니다. 도커 컨테이너를 이용하여 서버와 로컬에서 같은 환경을 유지할 수 있도록 만들었습니다.

인증/인가 처리는 google OAuth2 방식과 email 인증을 통한 회원 가입이 가능하며, jwt cookie를 기반으로 인가를 처리합니다.

AWS SDK를 통해 사진을 S3에 저장하였습니다. 




⚒️ Development
- Backend

## 업데이트 내역
>[Update.md](docs/Update.md)

## 발생한 문제점 및 해결
- JPA - N+1 문제
- DB데이터를 불러오는데 2s가 넘게 소요 되는 문제
- Entity를 Dto로 변환하는데 800ms가 소요 되는 문제
---
## API 문서
> [Api.md](docs/API/Api.md)

