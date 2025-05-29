package com.wheredidwego.repository;

import com.wheredidwego.domain.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {

    @Query(
            value = "SELECT * FROM tourist_spot FORCE INDEX (idx_lat_lng_nm_cd) " +
                    "WHERE lat BETWEEN :swLat AND :neLat " +
                    "AND lng BETWEEN :swLng AND :neLng " +
                    "AND (category_code < '60516' OR category_code > '60526')",
            nativeQuery = true
    )
    List<TouristSpot> findAllInBounds(@Param("swLat") double swLat, @Param("neLat") double neLat, @Param("swLng") double swLng, @Param("neLng") double neLng);
}

