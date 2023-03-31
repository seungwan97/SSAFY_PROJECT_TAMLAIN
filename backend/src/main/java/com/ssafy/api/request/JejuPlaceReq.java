package com.ssafy.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JejuPlaceReq {
    private int jejuPlaceId;
    private String name;
    private String category;
    private String categoryDetail;
    private Double latitude;
    private Double Longitude;
    private String roadAddress;
    private String placeUrl;
    private String imgUrl;
    private int reviewScoreSum;
    private int reviewCount;
    private String tag;

    @Builder
    public JejuPlaceReq(int jejuPlaceId, String name, String category, String categoryDetail, Double latitude, Double longitude, String roadAddress, String placeUrl, String imgUrl, int reviewScoreSum, int reviewCount, String tag) {
        this.jejuPlaceId = jejuPlaceId;
        this.name = name;
        this.category = category;
        this.categoryDetail = categoryDetail;
        this.latitude = latitude;
        Longitude = longitude;
        this.roadAddress = roadAddress;
        this.placeUrl = placeUrl;
        this.imgUrl = imgUrl;
        this.reviewScoreSum = reviewScoreSum;
        this.reviewCount = reviewCount;
        this.tag = tag;
    }
}
