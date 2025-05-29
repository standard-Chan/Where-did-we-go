
# 좌표 정렬 region
CREATE INDEX idx_lat_lng ON region(lat, lng);

# tourist spot
CREATE INDEX idx_lat_lng_nm_cd ON tourist_spot(lat asc, lng asc, name, category_code);