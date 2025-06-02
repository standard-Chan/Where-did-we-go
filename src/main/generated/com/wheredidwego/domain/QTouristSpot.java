package com.wheredidwego.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTouristSpot is a Querydsl query type for TouristSpot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTouristSpot extends EntityPathBase<TouristSpot> {

    private static final long serialVersionUID = -2059651043L;

    public static final QTouristSpot touristSpot = new QTouristSpot("touristSpot");

    public final EnumPath<com.wheredidwego.enumerate.TouristCategory> categoryCode = createEnum("categoryCode", com.wheredidwego.enumerate.TouristCategory.class);

    public final StringPath district = createString("district");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Double> lng = createNumber("lng", Double.class);

    public final StringPath name = createString("name");

    public final StringPath province = createString("province");

    public final StringPath subdistrict = createString("subdistrict");

    public QTouristSpot(String variable) {
        super(TouristSpot.class, forVariable(variable));
    }

    public QTouristSpot(Path<? extends TouristSpot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTouristSpot(PathMetadata metadata) {
        super(TouristSpot.class, metadata);
    }

}

