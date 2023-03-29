package com.ssafy.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FlaskJejuPlaceItem {
    private int jejuPlaceId;
    private String name;
    private String category;
    private String categoryDetail;
    private Double latitude;
    private Double longitude;
    private Double reviewScore;

    @Builder
    public FlaskJejuPlaceItem(int jejuPlaceId, String name, String category, String categoryDetail, Double latitude, Double longitude, Double reviewScore) {
        this.jejuPlaceId = jejuPlaceId;
        this.name = name;
        this.category = category;
        this.categoryDetail = categoryDetail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reviewScore = reviewScore;
    }
}
