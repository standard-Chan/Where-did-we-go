# 관광지
CREATE TABLE tourist_spot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_code VARCHAR(20) NOT NULL,
    lat DOUBLE NOT NULL,
    lng DOUBLE NOT NULL,
    province VARCHAR(10) NOT NULL,
    district VARCHAR(15),
    subdistrict VARCHAR(20)
);