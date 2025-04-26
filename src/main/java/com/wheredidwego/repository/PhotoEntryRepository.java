package com.wheredidwego.repository;

import com.wheredidwego.domain.PhotoEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoEntryRepository extends JpaRepository<PhotoEntry, Long> {
    Optional<PhotoEntry> getPhotoEntriesById(Long id);
}
