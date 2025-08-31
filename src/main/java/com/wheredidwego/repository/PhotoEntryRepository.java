package com.wheredidwego.repository;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.photoEntry.ProvincePhotoCountResponse;
import com.wheredidwego.repository.queryDSL.custom.PhotoEntryRepositoryCustom;
import com.wheredidwego.temp.PhotoEntryWithRegionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoEntryRepository extends JpaRepository<PhotoEntry, Long>, PhotoEntryRepositoryCustom {
    Optional<PhotoEntry> getPhotoEntriesById(Long id);

    @Query("SELECT p FROM PhotoEntry p JOIN FETCH p.region WHERE p.user = :user")
    List<PhotoEntry> findAllByUser(@Param("user") User user);

    @Query(value = """
        SELECT p.id,
               p.photo_path,
               p.description,
               p.taken_at,
               r.lat,
               r.lng,
               r.province,
               r.district,
               r.subdistrict
        FROM photo_entry p
        JOIN (
            SELECT id, lat, lng, province, district, subdistrict
            FROM region
            WHERE lat BETWEEN :swLat AND :neLat
              AND lng BETWEEN :swLng AND :neLng
        ) r ON p.region_id = r.id
        WHERE p.user_id = :userId
        """, nativeQuery = true)
    List<Object[]> findAllInBoundsNative(
            @Param("userId") Long userId,
            @Param("swLat") Double swLat,
            @Param("neLat") Double neLat,
            @Param("swLng") Double swLng,
            @Param("neLng") Double neLng
    );

    Page<PhotoEntry> findAllByUser(User user, Pageable pageable);

    @Query("SELECT new com.wheredidwego.dto.photoEntry.ProvincePhotoCountResponse(r.province, count(p)) FROM PhotoEntry p JOIN p.region r "+
    "WHERE p.user = :user GROUP BY r.province")
    List<ProvincePhotoCountResponse> findPhotoEntriesCountsPerProvince(@Param("user") User user);
}
