package com.wheredidwego.repository;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoEntryRepository extends JpaRepository<PhotoEntry, Long> {
    Optional<PhotoEntry> getPhotoEntriesById(Long id);

    @Query("SELECT p FROM PhotoEntry p JOIN FETCH p.region WHERE p.user = :user")
    List<PhotoEntry> findAllByUser(@Param("user") User user);

    @Query("SELECT p FROM PhotoEntry p JOIN FETCH p.region r " +
            "WHERE p.user = :user " +
            "AND r.lat BETWEEN :swLat AND :neLat " +
            "AND r.lng BETWEEN :swLng AND :neLng")
    List<PhotoEntry> findAllInBounds(@Param("user") User user,
                                     @Param("swLat") Double swLat,
                                     @Param("neLat") Double neLat,
                                     @Param("swLng") Double swLng,
                                     @Param("neLng") Double neLng);

    Page<PhotoEntry> findAllByUser(User user, Pageable pageable);
}
