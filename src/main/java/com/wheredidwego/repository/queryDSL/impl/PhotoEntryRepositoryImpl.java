package com.wheredidwego.repository.queryDSL.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.QPhotoEntry;
import com.wheredidwego.domain.QRegion;
import com.wheredidwego.domain.User;
import com.wheredidwego.repository.queryDSL.custom.PhotoEntryRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PhotoEntryRepositoryImpl implements PhotoEntryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PhotoEntry> findAllInBounds(User user, Double swLat, Double neLat, Double swLng, Double neLng) {

        QPhotoEntry qPhotoEntry = QPhotoEntry.photoEntry;
        QRegion qRegion = QRegion.region;

        return queryFactory
                .selectFrom(qPhotoEntry)
                .join(qPhotoEntry.region, qRegion).fetchJoin()
                .where(
                        qPhotoEntry.user.eq(user),
                        qRegion.lat.between(swLat, neLat),
                        qRegion.lng.between(swLng, neLng)
                )
                .fetch();
    }
}
