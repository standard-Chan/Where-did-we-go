# 발생한 문제점 및 해결

## 1. JPA - N+1 문제
파악 : 지나치게 많은 시간이 소요. 전체 데이터를 가져오는데 약 16s가 소요됨. 
-> log를 분석해보니 query가 과도하게 발생하고 있다는 것을 파악하여 N+1문자라는 것을 알게됨.

> 좌표 Entity와 사진 Entity를 join 해서 가져올 때, N+1 문제가 발생

- 원인 : Fetch 방식이 LAZY이기 때문에 발생.

해결방안
1. FETCH.EAGER로 변경
    - 비효율적.
2. **@Query를 사용하여 fetch join으로 가져오기** ✅



## 2. DB데이터를 불러오는데 1s가 넘게 소요 되는 문제
(총 더미데이터 10만개로 테스트)
파악 : N+1 문제를 해결하였음에도 약 1800ms가 소요되는 문제가 발생.
로깅과 MY SQL Workbench로 시간 분석 -> 속도를 약 800ms는 java 로직에서, 나머지 약 1s는 SQL fetch에서 발생함.

> 아래 query join 시에 한 region 테이블에서 FULL SCAN 하여 문제가 발생
```sql
SELECT *
        FROM photo_entry p
        JOIN region r ON p.region_id = r.id
        WHERE p.user_id = :userId
        AND lat BETWEEN :Lat1 AND :Lat2
        AND lng BETWEEN :Lng1 AND :Lng2
```

원인 (Query Plan 결과)
- photo_entry의 type : REF
- region type : ALL
- ALL에서 테이블을 FULL SCAN하므로 시간이 많이 소요됨.

해결 방안
1. INDEX를 생성하여 ALL 타입을 REF로 변경
   1. photo_entry INDEX(userId)로 REF로 변경 
   2. region INDEX (기본 primary)로 eq_REF로 변경
    
   - 장단점
     - 인덱스 2개를 사용했으므로 범위 검색 시에는 ALL SCAN으로 진행된다는 단점이 있다.
     - 따라서 region ID가 많을 경우, 속도가 느려질 수 있다.
     - 하지만 요청되는 정보는 화면 양 끝단의 lat, lng 범위 내일 것이므로
        region 정보가 많지 않을 가능성이 높다.
   
```sql
SELECT p.*
FROM photo_entry p
JOIN (
    SELECT id FROM region
    WHERE lat BETWEEN :Lat1 AND :Lat2
      AND lng BETWEEN :Lng1 AND :Lng2
) r ON p.region_id = r.id
WHERE p.user_id = :userId
```
2. region 먼저 필터링한 뒤 JOIN (subquery)
   1. photo_entry INDEX(userId)로 REF로 변경
   2. region의 좌표(lat, lng) 기준 INDEX를 생성하여 subquery로 필터링
   3. 이후 JOIN
   - 장단점
     - 마찬가지로 2개의 인덱스를 사용했으므로 조인 조건인 region_id를 읽기 위해
       필터링 된 모든 region 테이블을 읽어야함. 
     - 필터링되어 읽을 데이터가 많지 않으면 1번 방식보다 훨씬 빠르다.
     - 하지만 2번 방식 (from 절에 subquery)은 native query를 사용해야하기 때문에
       유지보수가 좋지 않는 문제가 있다.


실제 테스트에서는 위 두 방식 모두 시간적으로 큰 차이는 나지 않는다.


## 3. Entity를 Dto로 변환하는데 800ms가 소요 되는 문제

병렬 스트림을 통해 해결하였다.