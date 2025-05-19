SET SESSION cte_max_recursion_depth = 1000000;

# 원본 계정
INSERT INTO users (email, password, nickname, created_at)
value (
	'starp321@naver.com',
    '$2a$10$zl224haJzgEXkTTpnBU8gubM9L2UhLcGsQG9sXOujTrbVttwzmNa.',
    '석찬',
    NOW());

# region (33 < lat < 39 && 124 < lng < 132
INSERT INTO region (lat, lng, province, district, subdistrict,reference_count)
WITH RECURSIVE numbers AS (
    SELECT 1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 50000
)
SELECT
    33 + RAND()*6,
    124 + RAND()*8,
    '서울특별시',
    '동작구',
    '여의대방로44길 46',
    1 + RAND()*10
FROM numbers;

INSERT INTO photo_entry (user_id, region_id, photo_path, description, taken_at, created_at)
WITH RECURSIVE numbers AS (
    SELECT 1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 10
)
SELECT 
		1,
        1+MOD(n,10000),
        'starp321@naver.com/photoEntry/1747637330389',
        '설명',
        DATE_SUB(NOW(), INTERVAL MOD(n, 365) DAY),
        DATE_SUB(NOW(), INTERVAL MOD(n, 365) DAY)
FROM numbers;

# 가상 유저 만들기
INSERT INTO users (email, password, nickname, created_at)
WITH RECURSIVE numbers AS (
    SELECT 1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 10000
)
SELECT
    CONCAT('user', LPAD(n, 7, '0'), '@test.com'),
    '$2a$10$iWPQvWHXRaJvPpZn1qzp3.GKBHXa9mFQXj4.Nz.yu.YyXJxvxXnwi',
    CONCAT('User', LPAD(n, 7, '0')),
    NOW()
FROM numbers;

# 가상 사진 데이터
INSERT INTO photo_entry (user_id, region_id, photo_path, description, taken_at, created_at)
WITH RECURSIVE numbers AS (
    SELECT 1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 10000
)
SELECT
    1 + MOD(n, 10000),
    1 + MOD(n, 10000),
    '$2a$10$iWPQvWHXRaJvPpZn1qzp3.GKBHXa9mFQXj4.Nz.yu.YyXJxvxXnwi',
    CONCAT('User', LPAD(n, 7, '0')),
    DATE_SUB(NOW(), INTERVAL MOD(n, 365) DAY),
	DATE_SUB(NOW(), INTERVAL MOD(n, 365) DAY)
FROM numbers;

        
        