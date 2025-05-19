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

    Page<PhotoEntry> findAllByUser(User user, Pageable pageable);
}
