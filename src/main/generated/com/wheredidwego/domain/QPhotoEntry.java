package com.wheredidwego.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPhotoEntry is a Querydsl query type for PhotoEntry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhotoEntry extends EntityPathBase<PhotoEntry> {

    private static final long serialVersionUID = -584259721L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhotoEntry photoEntry = new QPhotoEntry("photoEntry");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath photoPath = createString("photoPath");

    public final QRegion region;

    public final DatePath<java.time.LocalDate> takenAt = createDate("takenAt", java.time.LocalDate.class);

    public final QUser user;

    public QPhotoEntry(String variable) {
        this(PhotoEntry.class, forVariable(variable), INITS);
    }

    public QPhotoEntry(Path<? extends PhotoEntry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPhotoEntry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPhotoEntry(PathMetadata metadata, PathInits inits) {
        this(PhotoEntry.class, metadata, inits);
    }

    public QPhotoEntry(Class<? extends PhotoEntry> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.region = inits.isInitialized("region") ? new QRegion(forProperty("region")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

