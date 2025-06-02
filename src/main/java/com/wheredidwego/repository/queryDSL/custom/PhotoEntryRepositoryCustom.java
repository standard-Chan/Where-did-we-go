package com.wheredidwego.repository.queryDSL.custom;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;

import java.util.List;

public interface PhotoEntryRepositoryCustom {
    List<PhotoEntry> findAllInBounds(User user, Double swLat, Double neLat, Double swLng, Double neLng);
}
