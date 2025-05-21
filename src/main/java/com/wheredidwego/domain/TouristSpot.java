package com.wheredidwego.domain;

import com.wheredidwego.domain.enumerate.TouristCategory;
import com.wheredidwego.util.TouristCategoryConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tourist_spot")
public class TouristSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Convert(converter = TouristCategoryConverter.class)
    @Column(name = "category_code", nullable = false, length = 20)
    private TouristCategory categoryCode;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false, length = 10)
    private String province;

    @Column(length = 15)
    private String district;

    @Column(length = 20)
    private String subdistrict;

}
