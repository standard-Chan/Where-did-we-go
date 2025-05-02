package com.wheredidwego.repository;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoEntryRepository extends JpaRepository<PhotoEntry, Long> {
    Optional<PhotoEntry> getPhotoEntriesById(Long id);

    List<PhotoEntry> findAllByUser(User user);

    Page<PhotoEntry> findAllByUser(User user, Pageable pageable);
}
