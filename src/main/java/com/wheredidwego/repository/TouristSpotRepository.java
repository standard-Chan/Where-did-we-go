package com.wheredidwego.repository;

import com.wheredidwego.domain.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {

    @Query("SELECT t FROM TouristSpot t WHERE t.lat BETWEEN :swLat AND :neLat AND t.lng BETWEEN :swLng AND :neLng")
    List<TouristSpot> findAllInBounds(@Param("swLat") double swLat, @Param("neLat") double neLat, @Param("swLng") double swLng, @Param("neLng") double neLng);
}

