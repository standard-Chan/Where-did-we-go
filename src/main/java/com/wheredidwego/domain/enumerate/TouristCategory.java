package com.wheredidwego.domain.enumerate;

import lombok.Getter;

@Getter
public enum TouristCategory {
    //TOURIST_INFO_CENTER("60401", "관광안내소/매표소"),
    //AIRLINE_TRAVEL_AGENCY("60403", "항공사/여행사"),
    ETC1("60400", "기타"),
    ETC2("60500", "기타2"),
    ETC3("60600", "기타3"),

    BEACH("60501", "해수욕장"),
    FOREST_GARDEN("60503", "휴양림/수목원"),
    WATERFALL_VALLEY("60504", "폭포/계곡"),
    HOT_SPRING("60505", "온천지역"),
    CAMPING_SITE("60506", "야영장"),

    THEME_PARK("60507", "테마공원/대형놀이공원"),
    GENERAL_PARK("60508", "일반유원지/일반놀이공원"),

    AQUARIUM("60509", "아쿠아리움/대형수족관"),
    ZOO("60510", "동물원"),
    BOTANIC_GARDEN("60511", "식물원"),

    GENERAL_SPOT("60513", "일반관광지"),
    POPULAR_SPOT("60514", "유명관광지"),

    DRAMA_MOVIE_SITE("60515", "드라마/영화촬영지"),
    HERB_FARM("60516", "관광농원/허브마을"),
    //FOOD_FASHION_STREET("60517", "먹거리/패션거리"),
    FARM_STAY("60518", "팜스테이"),
    IT_VILLAGE("60519", "정보화마을"),
    CAMPING_GROUND("60520", "캠핑장"),
    ENGLISH_VILLAGE("60522", "영어마을"),
    LOCAL_FESTIVAL("60523", "지역축제"),
    CAMPING_HOLIDAY("60524", "캠핑홀리데이(캠핑)"),
    JAMPING_HOLIDAY("60525", "잼핑홀리데이(캠핑)"),
    GLAMPING_KOREA("60526", "글램핑코리아(캠핑)"),

    PALACE("60601", "궁궐/종묘"),
    ROYAL_TOMB("60602", "왕릉/고분"),
    HISTORIC_SITE("60603", "유명사적/유적지"),
    NATIONAL_TREASURE("60604", "국보"),
    TREASURE("60605", "보물"),
    MONUMENT("60606", "비/탑/문/각"),
    NATURAL_MONUMENT("60607", "천연기념물"),
    TRADITIONAL_HOUSE("60608", "고택/생가/민속마을"),
    CONFUCIAN_SCHOOL("60609", "서원/향교/서당"),
    CASTLE("60610", "성/성터");

    private final String code;
    private final String description;

    TouristCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TouristCategory getCategoryFromCode(String code) {
        for (TouristCategory category : values()) {
            if (category.code.equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category code: " + code);
    }
}

